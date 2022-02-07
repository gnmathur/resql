package com.gmathur.resql.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcHandle.class);
    private final Connection connection;


    public JdbcHandle(String url, String username, String password) {
        try {
            //TODO Definitely not ok in a multi-threaded context but is it still ok to share a connection here between test runs
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLTimeoutException e) {
            throw new RuntimeException(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    ResultSet doQuery(String sql) throws SQLException {
        final Statement st = connection.createStatement();
        final ResultSet rs = st.executeQuery(sql);
        return rs;
    }
}