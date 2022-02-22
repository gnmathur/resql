package com.gmathur.resql.unit;

import com.gmathur.resql.Resql;
import com.gmathur.resql.exceptions.ResqlException;
import com.gmathur.resql.exceptions.ResqlExceptionHandler;
import com.gmathur.resql.translators.postgres.ResqlWhereProcessorPostgres;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResqlPgAdapterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResqlPgAdapterTest.class);

    private static class ResqlPgAdapterTestException extends ResqlException {
        public ResqlPgAdapterTestException(String message) {
            super(message);
        }
    }

    public static class TestParseException implements ResqlExceptionHandler {
        private static final TestParseException INSTANCE = new TestParseException();

        @Override
        public void report(String msg) throws ResqlException {
            LOGGER.info(msg);
            throw new ResqlPgAdapterTestException(msg);
        }
    }

    private final Resql w = Resql.builder()
            .withExceptionHandler(TestParseException.INSTANCE)
            .withWhereBuilder(ResqlWhereProcessorPostgres.class)
            .build();

    private void assertThrowsCheck(final String clause) {
        assertThrows(ResqlPgAdapterTestException.class, () -> {
            final String restWhereArg = clause;
            final Optional<String> res1 = w.process(restWhereArg);
        });
    }

    @Test
    public void testGreaterThanOnly() {
        {
            final String restWhereArg = "f1 > 10";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals("f1 > 10", res.get());
        }
        {
            final String restWhereArg = "f1    >    10";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals("f1 > 10", res.get());
        }
        {
            final String restWhereArg = "f1>10";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals("f1 > 10", res.get());
        }
    }

    @Test
    public void testLessThanOnly() {
        {
            final String restWhereArg1 = "f1 < 17";
            final Optional<String> res = w.process(restWhereArg1);
            assertTrue(res.isPresent());
            assertEquals("f1 < 17", res.get());
        }
        {
            final String restWhereArg2 = "f1<17";
            final Optional<String> res2 = w.process(restWhereArg2);
            assertTrue(res2.isPresent());
            assertEquals("f1 < 17", res2.get());
        }
        {
            final String restWhereArg2 = "f1    <       17";
            final Optional<String> res2 = w.process(restWhereArg2);
            assertTrue(res2.isPresent());
            assertEquals("f1 < 17", res2.get());
        }
    }

    @Test
    public void testInBetween() {
        final String restWhereArg1 = "age ><(20, 31)";
        final String expected1 = "(age >= 20 AND age < 31)";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());

        final String restWhereArg2 = "age  ><  (20,   31)";
        final String expected2 = "(age >= 20 AND age < 31)";
        final Optional<String> res2 = w.process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals(expected2, res2.get());
    }

    @Test
    public void testIn() {
        final String restWhereArg1 = "size ^ [3, 5, 7,   11, 13]";
        final String expected1 = "size IN (3,5,7,11,13)";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }

    @Test
    public void testGreaterThanAndLessThanCombinations() {
        final String restWhereArg1 = "age > 10  && size  <   100";
        final String expected1 = "age > 10 AND size < 100";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());

        final String restWhereArg2 = "age < 50    && size  > 100";
        final String expected2 = "age < 50 AND size > 100";
        final Optional<String> res2 = w.process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals(expected2, res2.get());

        final String restWhereArg3 = "age > 50 && size > 10";
        final String expected3 = "age > 50 AND size > 10";
        final Optional<String> res3 = w.process(restWhereArg3);
        assertTrue(res3.isPresent()); assertEquals(expected3, res3.get());

        final String restWhereArg4 = "age < 50 && size < 10";
        final String expected4 = "age < 50 AND size < 10";
        final Optional<String> res4 = w.process(restWhereArg4);
        assertTrue(res4.isPresent()); assertEquals(expected4, res4.get());
    }

    @Test
    public void testFloatingPoint() {
        {
            final String restWhereArg = "width_ft > 10.22 && length  <= 100.2  && height_ft <= 0.22";
            final String expected = "width_ft > 10.22 AND length <= 100.2 AND height_ft <= 0.22";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }

        {
            final String restWhereArg = "(width_ft > 10.22 || length  <= 100.2) && height_ft <= 0.22";
            final String expected = "(width_ft > 10.22 OR length <= 100.2) AND height_ft <= 0.22";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "f1 > 10.111 && f2 <= 1111.2222 || f3 !^[5.5, 6.6]";
            final String expected = "f1 > 10.111 AND f2 <= 1111.2222 OR f3 NOT IN (5.5,6.6)";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
    }

    @Test
    public void testLogicalAnd() {
        final String restWhereArg1 = "age < 50 && size < 10 || city = 'fremont'";
        final String expected1 = "age < 50 AND size < 10 OR city = 'fremont'";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }


    @Test
    public void testComplexWhere() {
        final String restWhereArg = "(f1 = 10 && (f2 != 11 || f3 = 12) && (f4 > 13)) || (f5 = 'hello') || (f6 ^[321, 11, 17]) || f7 ><(1, 100)";
        final String expected = "(f1 = 10 AND (f2 != 11 OR f3 = 12) AND (f4 > 13)) OR (f5 = 'hello') OR (f6 IN (321,11,17)) OR (f7 >= 1 AND f7 < 100)";

        final Optional<String> res = w.process(restWhereArg);

        assertTrue(res.isPresent());
        assertEquals(expected, res.get());
    }

    @Test
    public void testThatFieldsCanHaveUnderscore() {
        final String restWhereArg1 = "rental_length > 10";
        final String expected1 = "rental_length > 10";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }

    @Test
    public void testInvalidClauses() {
        assertThrowsCheck("rental_length > 10 ?");
        assertThrowsCheck("rental_length");
        assertThrowsCheck("rental_length == 10");
        assertThrowsCheck("rental_length === 10");
        assertThrowsCheck("age << 10"); // unknown operator
        assertThrowsCheck("age >> 10"); // unknown operator
        assertThrowsCheck("a?ge > 10"); // non-identifier character
        assertThrowsCheck("?age > 10"); // extraneous leading ?
        assertThrowsCheck("age ^(10, 11)"); // in clause expects in set in brackets
        assertThrowsCheck("age ^[10, 11, 12"); // missing terminating bracket
        assertThrowsCheck("age BTW[10, 11]"); // brackets instead of paren
        assertThrowsCheck("age BTW((10, 11)"); // extraneous paren
        assertThrowsCheck("age BTW0, 11"); // missing paren
        assertThrowsCheck("age BTW10, 11");
    }

    @Test
    public void testLikeClause() {
        final String restWhereArg1 = "foo ~ '%somestring%'";
        final String expected1 = "foo LIKE '%somestring%'";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }

    @Test
    public void testInAndNotInClauses() {
        {
            final String restWhereArg = "foo !^[1, 2, 3]";
            final String expected = "foo NOT IN (1,2,3)";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "foo ^[1, 2, 3]";
            final String expected = "foo IN (1,2,3)";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "f1 ^[1, 2, 3] && f2 !^['a', 'b', 'c'] || f3 ^[1.1, 1.2]";
            final String expected = "f1 IN (1,2,3) AND f2 NOT IN ('a','b','c') OR f3 IN (1.1,1.2)";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
    }

    @Test
    public void testStringLiterals() {
        {
            final String restWhereArg = "foo_bar = '$avalue'";
            final String expected = "foo_bar = '$avalue'";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "foo_bar = '$!@#$%^&*()+_[]{}abcd)(*&^%#@!$'";
            final String expected= "foo_bar = '$!@#$%^&*()+_[]{}abcd)(*&^%#@!$'";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "foo_bar ~ '$avalue'";
            final String expected = "foo_bar LIKE '$avalue'";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
        {
            final String restWhereArg = "foo_bar = ''";
            final String expected = "foo_bar = ''";
            final Optional<String> res = w.process(restWhereArg);
            assertTrue(res.isPresent());
            assertEquals(expected, res.get());
        }
    }
}
