package com.example.tpfinaltusi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IntegrationDB {
    private static final String MYSQL_URL = "jdbc:mysql://containers-us-west-120.railway.app:6627/railway";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "JtKOArH8PGF0aoIXZeGO";

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean testConnection() {
        Connection connection = connect();
        if (connection != null) {
            disconnect(connection);
            return true;
        }
        return false;
    }
}
