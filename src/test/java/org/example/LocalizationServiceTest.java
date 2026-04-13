package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void getStringsShouldReturnMapWithValues() throws Exception {
        LocalizationService service = new LocalizationService();

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("key")).thenReturn("title", "button");
        when(rs.getString("value")).thenReturn("Shopping Cart", "Save");

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(conn);

            Map<String, String> result = service.getStrings("en");

            assertEquals(2, result.size());
            assertEquals("Shopping Cart", result.get("title"));
            assertEquals("Save", result.get("button"));
            verify(stmt).setString(1, "en");
        }
    }

    @Test
    void getStringsShouldReturnEmptyMapWhenNoRowsFound() throws Exception {
        LocalizationService service = new LocalizationService();

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(stmt);
        when(stmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(conn);

            Map<String, String> result = service.getStrings("en");

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(stmt).setString(1, "en");
        }
    }

    @Test
    void getStringsShouldReturnEmptyMapWhenExceptionOccurs() {
        LocalizationService service = new LocalizationService();

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("DB error"));

            Map<String, String> result = service.getStrings("en");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}