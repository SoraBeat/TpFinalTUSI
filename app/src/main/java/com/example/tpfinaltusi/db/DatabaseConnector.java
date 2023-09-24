package com.example.tpfinaltusi.db;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // Datos de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://containers-us-west-120.railway.app:6627/railway";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "JtKOArH8PGF0aoIXZeGO";

    // Interfaz para manejar eventos de conexión
    public interface ConnectionListener {
        void onConnectionSuccess();
        void onConnectionError(String error);
    }

    // Método para conectar a la base de datos en segundo plano
    public static void connectInBackground(ConnectionListener listener) {
        new ConnectToDatabaseTask(listener).execute();
    }

    // Clase AsyncTask para la conexión a la base de datos en segundo plano
    private static class ConnectToDatabaseTask extends AsyncTask<Void, Void, Boolean> {
        private ConnectionListener listener;
        private String errorMessage = null;

        public ConnectToDatabaseTask(ConnectionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");// Cargar el controlador JDBC

                // Establecer la conexión a la base de datos
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                connection.close(); // Cerrar la conexión inmediatamente

                return true; // Conexión exitosa
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                errorMessage = "Error al cargar el controlador de la base de datos.";
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage = "Error de conexión a la base de datos.";
            }
            return false; // Error de conexión
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // La conexión fue exitosa, llama al método onConnectionSuccess del listener
                listener.onConnectionSuccess();
            } else {
                // Hubo un error en la conexión, llama al método onConnectionError del listener
                listener.onConnectionError(errorMessage);
            }
        }
    }
}
