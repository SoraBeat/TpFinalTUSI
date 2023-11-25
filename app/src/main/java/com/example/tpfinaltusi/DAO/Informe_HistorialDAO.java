package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Informe_Historial;
import com.example.tpfinaltusi.entidades.Informe_Imagen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Informe_HistorialDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera
    public Informe_HistorialDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                Informe_HistorialDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("Informe_Historial", error);
                connectionLatch.countDown(); // Indica que la conexión ha fallado
            }
        });
    }
    private void esperarConexion() {
        try {
            connectionLatch.await(); // Espera hasta que la conexión se establezca
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean crearInforme_Historial(Informe_Historial informe_historial) {
        esperarConexion();
        String sql = "INSERT INTO informe_historial (idinforme, imagen, imagenprueba, fecha, idestado, titulo, cuerpo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, informe_historial.getIdInforme());
            statement.setString(2, informe_historial.getIMG());
            statement.setString(3, informe_historial.getIMG_Prueba());
            statement.setDate(4,  new java.sql.Date(System.currentTimeMillis()));
            statement.setInt(5, informe_historial.getIdEstado());
            statement.setString(6, informe_historial.getTitulo());
            statement.setString(7, informe_historial.getCuerpo());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean ocultarInforme_Imagen(int IdInforme_Historial) {
        esperarConexion();
        String sql = "UPDATE informe_historial SET ocultar=? WHERE IdInforme_Historial=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, true);
            statement.setInt(2, IdInforme_Historial);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Informe_Historial traerInforme_HistorialPorId(int idInforme) {
        esperarConexion();
        String sql = "SELECT * FROM informe_historial WHERE IdInforme=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInforme);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearInforme_HistorialDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Informe_Historial> traerTodosLosInformes_Historial() {
        esperarConexion();
        List<Informe_Historial> informes_historial = new ArrayList<>();
        String sql = "SELECT * FROM informe_historial";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                informes_historial.add(crearInforme_HistorialDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return informes_historial;
    }
    private Informe_Historial crearInforme_HistorialDesdeResultSet(ResultSet resultSet) throws SQLException {
        Informe_Historial informe_historial = new Informe_Historial();
        informe_historial.setIdInforme_Historial(resultSet.getInt("idInforme_Historial"));
        informe_historial.setIdInforme(resultSet.getInt("idInforme"));
        informe_historial.setIMG(resultSet.getString("imagen"));
        informe_historial.setIMG_Prueba(resultSet.getString("imagenPrueba"));
        informe_historial.setFecha(resultSet.getDate("fecha"));
        informe_historial.setIdEstado(resultSet.getInt("idEstado"));
        informe_historial.setTitulo(resultSet.getString("Titulo"));
        informe_historial.setCuerpo(resultSet.getString("Cuerpo"));
        informe_historial.setOcultar(resultSet.getBoolean("Ocultar"));
        return informe_historial;
    }
}
