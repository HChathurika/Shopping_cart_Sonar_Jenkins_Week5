package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {

    private static final Logger LOGGER = Logger.getLogger(LocalizationService.class.getName());

    public Map<String, String> getStrings(String language) {
        Map<String, String> strings = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    strings.put(rs.getString("key"), rs.getString("value"));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load localization strings.", e);
        }

        return strings;
    }
}