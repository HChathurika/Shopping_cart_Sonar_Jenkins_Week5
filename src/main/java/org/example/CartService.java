package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartService {

    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());

    public void saveCart(int totalItems, double totalCost, String language, List<CartItem> items) {
        String cartSql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int cartId = 0;

            try (PreparedStatement cartStmt =
                         conn.prepareStatement(cartSql, Statement.RETURN_GENERATED_KEYS)) {
                cartStmt.setInt(1, totalItems);
                cartStmt.setDouble(2, totalCost);
                cartStmt.setString(3, language);
                cartStmt.executeUpdate();

                try (ResultSet keys = cartStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        cartId = keys.getInt(1);
                    }
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (CartItem item : items) {
                    itemStmt.setInt(1, cartId);
                    itemStmt.setInt(2, item.getItemNumber());
                    itemStmt.setDouble(3, item.getPrice());
                    itemStmt.setInt(4, item.getQuantity());
                    itemStmt.setDouble(5, item.getSubtotal());
                    itemStmt.executeUpdate();
                }
            }

            conn.commit();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save cart data.", e);
        }
    }
}