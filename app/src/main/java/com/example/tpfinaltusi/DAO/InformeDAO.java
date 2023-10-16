package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Informe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class InformeDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public InformeDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                InformeDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("InformeDAO", error);
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

    // Crear un nuevo informe
    public boolean crearInforme(Informe informe) {
        esperarConexion();
        String sql = "INSERT INTO informes (Titulo, Cuerpo, UsuarioAlta, idEstado, latitud, longitud, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, informe.getTitulo());
            statement.setString(2, informe.getCuerpo());
            statement.setInt(3, informe.getUsuarioAlta());
            statement.setInt(4, informe.getIdEstado());
            statement.setDouble(5, informe.getLatitud());
            statement.setDouble(6, informe.getLongitud());
            statement.setString(7, informe.getImagen());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un informe existente
    public boolean editarInforme(Informe informe) {
        esperarConexion();
        String sql = "UPDATE informes SET Titulo=?, Cuerpo=?, IdNivel=?, FechaAlta=?, UsuarioAlta=?, FechaBaja=?, UsuarioBaja=?, PuntosRecompensa=? WHERE IdInforme=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, informe.getTitulo());
            statement.setString(2, informe.getCuerpo());
            statement.setInt(3, informe.getIdNivel());
            statement.setDate(4, new java.sql.Date(informe.getFechaAlta().getTime()));
            statement.setInt(5, informe.getUsuarioAlta());
            statement.setDate(6, new java.sql.Date(informe.getFechaBaja().getTime()));
            statement.setInt(7, informe.getUsuarioBaja());
            statement.setInt(8, informe.getPuntosRecompensa());
            statement.setInt(9, informe.getIdInforme());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un informe por IdInforme
    public boolean borrarInforme(int idInforme) {
        esperarConexion();
        String sql = "DELETE FROM informes WHERE IdInforme=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInforme);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un informe por IdInforme
    public Informe traerInformePorId(int idInforme) {
        esperarConexion();
        String sql = "SELECT * FROM informes WHERE IdInforme=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInforme);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearInformeDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todos los informes
    public List<Informe> traerTodosLosInformes() {
        esperarConexion();
        List<Informe> informes = new ArrayList<>();
        String sql = "SELECT * FROM informes";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                informes.add(crearInformeDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return informes;
    }

    // Método auxiliar para crear un objeto Informe desde un ResultSet
    private Informe crearInformeDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idInforme = resultSet.getInt("IdInforme");
        String titulo = resultSet.getString("Titulo");
        String cuerpo = resultSet.getString("Cuerpo");
        int idNivel = resultSet.getInt("IdNivel");
        Date fechaAlta = resultSet.getDate("FechaAlta");
        int usuarioAlta = resultSet.getInt("UsuarioAlta");
        Date fechaBaja = resultSet.getDate("FechaBaja");
        int usuarioBaja = resultSet.getInt("UsuarioBaja");
        int puntosRecompensa = resultSet.getInt("PuntosRecompensa");
        int idEstado = resultSet.getInt("IdEstado");
        double latitud = resultSet.getDouble("Latitud");
        double longitud = resultSet.getDouble("Longitud");
        String imagen = resultSet.getString("Imagen");
        return new Informe(idInforme, titulo, cuerpo, idNivel, fechaAlta, usuarioAlta, fechaBaja, usuarioBaja, puntosRecompensa, idEstado, latitud, longitud, imagen);
    }
}
