package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void shouldConnectToWorldDatabase() throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {

            assertNotNull(connection);
            assertFalse(connection.isClosed());

            DatabaseMetaData metadata = connection.getMetaData();

            assertTrue(metadata.getURL().contains("/world"));

            try (ResultSet tables = connection.getMetaData()
                    .getTables("world", null, "country", null)) {

                assertTrue(tables.next(),
                        "The country table should exist in the world database");
            }
        }
    }
}