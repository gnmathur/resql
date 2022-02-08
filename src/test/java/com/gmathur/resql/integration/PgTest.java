package com.gmathur.resql.integration;

import com.gmathur.resql.ResqlWhereBuilder;
import com.gmathur.resql.ResqlWhereBuilderPg;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Expected values are inferred from the migration ABV0.2__init.sql
@Testcontainers
public class PgTest {
    /*
    private static final Logger LOGGER = LoggerFactory.getLogger(PgTest.class);
    private static final String POSTGRES_USER = "resql";
    private static final String POSTGRES_PASSWORD = "resql";
    private static final String POSTGRES_DB = "resql";
    private static final String POSTGRES_NETWORK_ALIASES = "resqlpgnw";
    // TODO.md make PG work on a non-default port
    //private static final Integer POSTGRES_EXPOSED_PORT = 54321;
    // ??? This most likely is not needed as there are no other containers to talk to
    private static Network resqlNetwork = Network.newNetwork();
    private static JdbcHandle jdbcHandle;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine")
            .withDatabaseName(POSTGRES_DB)
            // TODO.md make PG work on a non-default port
      //      .withExposedPorts(POSTGRES_EXPOSED_PORT)
            .withNetwork(resqlNetwork)
            .withNetworkAliases(POSTGRES_NETWORK_ALIASES)
            .withUsername(POSTGRES_USER)
            .withPassword(POSTGRES_PASSWORD);

    @BeforeAll
    public static void init() {
        LOGGER.info("Initializing PG JDBC connection to {}", postgreSQLContainer.getJdbcUrl());
        jdbcHandle = new JdbcHandle(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());

        Flyway flyway =
                Flyway.configure()
                        .dataSource(
                                postgreSQLContainer.getJdbcUrl(),
                                postgreSQLContainer.getUsername(),
                                postgreSQLContainer.getPassword())
                        .load();
        flyway.migrate();
    }

    @Test
    public void dummyQueryTest() throws SQLException {
        ResultSet rs = jdbcHandle.doQuery("Select 1");
        while(rs.next()) {
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    public void gtGtLtSameFieldTest() throws SQLException {
        ResqlWhereBuilder w = new ResqlWhereBuilderPg();
        String where = w.process("length > 52 && length <57").get();
        ResultSet rs = jdbcHandle.doQuery("select film_id from film where " + where);
        Set<Integer> got = new HashSet<>();
        Set<Integer> expected = new HashSet<>(Arrays.asList(8, 66, 97, 110, 164, 199));
        while (rs.next()) {
            got.add(rs.getInt(1));
        }
        assertEquals(expected, got);
    }

    @Test
    public void gtGtLtDifferentFieldsTest() throws SQLException {
        ResqlWhereBuilder w = new ResqlWhereBuilderPg();
        String where = w.process("length >170   &&  rental_duration >  6").get();
        ResultSet rs = jdbcHandle.doQuery("select film_id from film where " + where);
        Set<Integer> got = new HashSet<>();
        // Expected values are inferred from the migration ABV0.2__init.sql
        Set<Integer> expected = new HashSet<>(Arrays.asList(27, 88, 94, 128, 182));
        while (rs.next()) {
            got.add(rs.getInt(1));
        }
        assertEquals(expected, got);
    }

    @Test
    public void parenOrAndAndTest() throws SQLException {
        final String clause = "(rating == 'G'||rating ==  'PG') && (length > 180)";
        ResqlWhereBuilder w = new ResqlWhereBuilderPg();
        String where = w.process(clause).get();
        ResultSet rs = jdbcHandle.doQuery("select film_id from film where " + where);
        Set<Integer> got = new HashSet<>();
        // Expected values are inferred from the migration ABV0.2__init.sql
        Set<Integer> expected = new HashSet<>(Arrays.asList(50, 128, 182));
        while (rs.next()) {
            got.add(rs.getInt(1));
        }
        assertEquals(expected, got);
    }

    @Test
    public void strInTestWithAndClauses() throws SQLException {
        final String clause = "rating ^['G', 'PG'] && film_id > 10 && film_id < 20";
        ResqlWhereBuilder w = new ResqlWhereBuilderPg();
        String where = w.process(clause).get();
        ResultSet rs = jdbcHandle.doQuery("select film_id from film where " + where);
        Set<Integer> got = new HashSet<>();
        Set<Integer> expected = new HashSet<>(Arrays.asList(11, 12, 13, 19));
        while (rs.next()) {
            got.add(rs.getInt(1));
        }
        assertEquals(expected, got);
    }

    @Test
    public void intInTestWithAndClauses() throws SQLException {
        final String clause = "length ^[123, 124, 125, 127]";
        ResqlWhereBuilder w = new ResqlWhereBuilderPg();
        String where = w.process(clause).get();
        ResultSet rs = jdbcHandle.doQuery("select film_id from film where " + where);
        Set<Integer> got = new HashSet<>();
        Set<Integer> expected = new HashSet<>(Arrays.asList(36, 95, 105, 145, 158));
        while (rs.next()) {
            got.add(rs.getInt(1));
        }
        assertEquals(expected, got);
    }

     */
}
