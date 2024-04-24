package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticateManager {
    private static int userId;

    public static int getUserId() {
        return userId;
    }

    public static void authenticateUser(String username, String password) throws SQLException {
        try (Connection connection = MySQLConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT userid FROM users WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("userid");
                System.out.println("User authenticated with userId: " + userId);
            } else {
                System.out.println("Authentication failed");
            }
        }
    }
}

