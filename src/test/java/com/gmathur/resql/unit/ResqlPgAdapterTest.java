package com.gmathur.resql.unit;

import com.gmathur.resql.ResqlWhereBuilder;
import com.gmathur.resql.ResqlWhereBuilderPg;
import com.gmathur.resql.exceptions.ResqlParseException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResqlPgAdapterTest {
    private ResqlWhereBuilder w = new ResqlWhereBuilderPg();

    @Test
    public void tc1GreaterThanOnlyAndIgnoringWhitespace() {
        final String restWhereArg = "f1    >    10";
        final Optional<String> res = w.process(restWhereArg);
        assertTrue(res.isPresent()); assertEquals("f1 > 10", res.get());
    }

    @Test
    public void tc2LessThanOnlyAndNoWhitespace() {
        final String restWhereArg1 = "f1<17";
        final Optional<String> res = w.process(restWhereArg1);
        assertTrue(res.isPresent()); assertEquals("f1 < 17", res.get());

        final String restWhereArg2 = "f1   <   17";
        final Optional<String> res2 = w.process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals("f1 < 17", res2.get());
    }

    @Test
    public void tc3InBetween() {
        final String restWhereArg1 = "age ><(20, 31)";
        final String expected1 = "(age >= 20 AND age < 31)";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());

        final String restWhereArg2 = "age  ><   (20,   31)";
        final String expected2 = "(age >= 20 AND age < 31)";
        final Optional<String> res2 = w.process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals(expected2, res2.get());
    }

    @Test
    public void tc4InWithWhitespace() {
        final String restWhereArg1 = "size ^^ [3, 5, 7,   11, 13]";
        final String expected1 = "size IN (3,5,7,11,13)";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }

    @Test
    public void tc5gtAndLtCombinations() {
        final String restWhereArg1 = "age >10  && size  <   100";
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
    public void tc6FloatingPoint() {
        final String restWhereArg1 = "width_ft > 10.22 && length  <= 100.2  && height_ft <= 0.22";
        final String expected1 = "width_ft > 10.22 AND length >= 100.2 AND height_ft >= 0.22";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());

        final String restWhereArg2 = "(width_ft > 10.22 || length  <= 100.2)&&height_ft <= 0.22";
        final String expected2 = "(width_ft > 10.22 OR length >= 100.2) AND height_ft >= 0.22";
        final Optional<String> res2 = w.process(restWhereArg2);
        assertTrue(res2.isPresent()); assertEquals(expected2, res2.get());
    }
  
    @Test
    public void tc7LogicalAnd() {
        final String restWhereArg1 = "age < 50 && size < 10 || city == 'fremont'";
        final String expected1 = "age < 50 AND size < 10 OR city = 'fremont'";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }


    @Test
    public void tc8ComplexWhere() {
        final String restWhereArg = "(f1 == 10 && (f2 != 11 || f3 == 12) && (f4 > 13)) || (f5 == 'hello') || (f6 ^^[321, 11, 17]) || f7 ><(1, 100)";
        final String expected = "(f1 = 10 AND (f2 != 11 OR f3 = 12) AND (f4 > 13)) OR (f5 = 'hello') OR (f6 IN (321,11,17)) OR (f7 >= 1 AND f7 < 100)";

        final Optional<String> res = w.process(restWhereArg);

        assertTrue(res.isPresent());
        assertEquals(expected, res.get());
    }

    @Test
    public void tc9FieldsCanHaveUnderscore() {
        final String restWhereArg1 = "rental_length > 10";
        final String expected1 = "rental_length > 10";
        final Optional<String> res1 = w.process(restWhereArg1);
        assertTrue(res1.isPresent()); assertEquals(expected1, res1.get());
    }
  
    private void assertThrowsCheck(final String clause) {
        assertThrows(ResqlParseException.class, () -> {
            final String restWhereArg1 = clause;
            final Optional<String> res1 = w.process(restWhereArg1);
        });
    }
    @Test
    public void tc10Invalid() {
        assertThrowsCheck("rental_length > 10 ?");
        assertThrowsCheck("rental_length");
        assertThrowsCheck("rental_length = 10");
        assertThrowsCheck("rental_length === 10");
        assertThrowsCheck("age << 10"); // unknown operator
        assertThrowsCheck("age >> 10"); // unknown operator
        assertThrowsCheck("a?ge > 10"); // non-identifier character
        assertThrowsCheck("?age > 10"); // extraneous leading ?
        assertThrowsCheck("age ^^(10, 11)"); // in clause expects in set in brackets
        assertThrowsCheck("age ^^[10, 11, 12"); // missing terminating bracket
        assertThrowsCheck("age ><[10, 11]"); // brackets instead of paren
        assertThrowsCheck("age ><((10, 11)"); // extraneous paren
        assertThrowsCheck("age ><10, 11"); // missing paren
        assertThrowsCheck("age ><10, 11");
    }
}
