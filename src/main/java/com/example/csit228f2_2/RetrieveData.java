package com.example.csit228f2_2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RetrieveData {
    public static void getData() {
        try(Connection c = MySQLConnection.getConnection();
                Statement statement = c.createStatement()) {

            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
