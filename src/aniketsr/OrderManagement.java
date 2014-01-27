package aniketsr;

import java.sql.*;

public class OrderManagement {
    public Connection getConnection(Connection connection) {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "aniketsr", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}