package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Test
    void saveCartShouldInsertCartAndItemsAndCommit() throws Exception {
        CartService cartService = new CartService();

        CartItem item1 = new CartItem(1, 100.0, 2, 200.0);
        CartItem item2 = new CartItem(2, 50.0, 3, 150.0);
        List<CartItem> items = List.of(item1, item2);

        Connection conn = mock(Connection.class);
        PreparedStatement cartStmt = mock(PreparedStatement.class);
        PreparedStatement itemStmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(cartStmt);
        when(conn.prepareStatement(anyString())).thenReturn(itemStmt);
        when(cartStmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(true);
        when(keys.getInt(1)).thenReturn(10);

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(conn);

            assertDoesNotThrow(() -> cartService.saveCart(5, 350.0, "en", items));

            verify(conn).setAutoCommit(false);
            verify(cartStmt).setInt(1, 5);
            verify(cartStmt).setDouble(2, 350.0);
            verify(cartStmt).setString(3, "en");
            verify(cartStmt).executeUpdate();
            verify(itemStmt, times(2)).executeUpdate();
            verify(conn).commit();
        }
    }

    @Test
    void saveCartShouldHandleExceptionWithoutThrowing() {
        CartService cartService = new CartService();
        List<CartItem> items = List.of(new CartItem(1, 100.0, 1, 100.0));

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection)
                    .thenThrow(new RuntimeException("Database error"));

            assertDoesNotThrow(() -> cartService.saveCart(1, 100.0, "en", items));
        }
    }

    @Test
    void saveCartShouldStillWorkWhenNoGeneratedKeyIsReturned() throws Exception {
        CartService cartService = new CartService();

        CartItem item = new CartItem(1, 100.0, 2, 200.0);
        List<CartItem> items = List.of(item);

        Connection conn = mock(Connection.class);
        PreparedStatement cartStmt = mock(PreparedStatement.class);
        PreparedStatement itemStmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(cartStmt);
        when(conn.prepareStatement(anyString())).thenReturn(itemStmt);
        when(cartStmt.getGeneratedKeys()).thenReturn(keys);
        when(keys.next()).thenReturn(false);

        try (MockedStatic<DatabaseConnection> mockedDb = mockStatic(DatabaseConnection.class)) {
            mockedDb.when(DatabaseConnection::getConnection).thenReturn(conn);

            assertDoesNotThrow(() -> cartService.saveCart(1, 200.0, "en", items));

            verify(cartStmt).executeUpdate();
            verify(itemStmt).setInt(1, 0);
            verify(itemStmt).setInt(2, 1);
            verify(itemStmt).setDouble(3, 100.0);
            verify(itemStmt).setInt(4, 2);
            verify(itemStmt).setDouble(5, 200.0);
            verify(itemStmt).executeUpdate();
            verify(conn).commit();
        }
    }
}