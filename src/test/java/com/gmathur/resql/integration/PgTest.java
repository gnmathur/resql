package com.gmathur.resql.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;


@Testcontainers
public class PgTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PgTest.class);
    private static final String POSTGRES_USER = "resql";
    private static final String POSTGRES_PASSWORD = "resql";
    private static final String POSTGRES_DB = "resql";
    private static final String POSTGRES_NETWORK_ALIASES = "resqlpgnw";
    private static final Integer POSTGRES_EXPOSED_PORT = 54321;
    // ??? This most likely is not needed as there are no other containers to talk to
    private static Network resqlNetwork = Network.newNetwork();

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName(POSTGRES_DB)
            .withExposedPorts(POSTGRES_EXPOSED_PORT)
            .withNetwork(resqlNetwork)
            .withNetworkAliases(POSTGRES_NETWORK_ALIASES)
            .withUsername(POSTGRES_USER)
            .withPassword(POSTGRES_PASSWORD);

    @BeforeAll
    public static void init() {
        LOGGER.info(postgreSQLContainer.getExposedPorts().toString());
    }

    @Test
    public void testThis() {
        LOGGER.info(postgreSQLContainer.getExposedPorts().toString());
        LOGGER.info(postgreSQLContainer.getDatabaseName());
    }
}
