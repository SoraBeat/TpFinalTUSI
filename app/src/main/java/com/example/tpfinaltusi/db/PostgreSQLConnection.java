package com.example.tpfinaltusi.db;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {

    public static void connectToDatabase(DatabaseConnectionCallback callback) {
        new Thread(() -> {
            Connection connection = null;
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://ep-sparkling-mouse-50878421.us-west-2.retooldb.com:5432/retool";
                String user = "retool";
                String password = "pqXjBcw9i8mo";
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    System.out.println("CONECCION EXITOSA");
                    callback.onConnectionSuccess(connection);
                } else {
                    callback.onConnectionFailure("Error al establecer la conexión a la base de datos.");
                }
            }
        }).start();
    }

    public interface DatabaseConnectionCallback {
        void onConnectionSuccess(Connection connection);
        void onConnectionFailure(String error);
    }
}
