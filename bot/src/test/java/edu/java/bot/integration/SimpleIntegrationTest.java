package edu.java.bot.integration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleIntegrationTest extends IntegrationEnvironment {
    @ParameterizedTest
    @ValueSource(strings = {"chat"})
    void tableExists(String tableName) throws SQLException {
        connection = DriverManager.getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        assertTrue(connection.getMetaData().getTables(null, null, tableName, null).next());
    }
}
