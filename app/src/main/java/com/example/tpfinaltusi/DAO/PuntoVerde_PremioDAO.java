package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.PuntoVerde_Premio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PuntoVerde_PremioDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public PuntoVerde_PremioDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                PuntoVerde_PremioDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("PuntoVerde_PremioDAO", error);
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

    // Crear una nueva relación PuntoVerde_Premio
    public boolean crearPuntoVerde_Premio(PuntoVerde_Premio puntoVerde_Premio) {
        esperarConexion();
        String sql = "INSERT INTO punto_verde_premio (idpuntoverdepremio, idpuntoverde, idpremio, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, puntoVerde_Premio.getIdPuntoVerdePremio());
            statement.setInt(2, puntoVerde_Premio.getIdPuntoVerde());
            statement.setInt(3, puntoVerde_Premio.getIdPremio());
            statement.setInt(4, puntoVerde_Premio.getStock());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una relación PuntoVerde_Premio existente
    public boolean editarPuntoVerde_Premio(PuntoVerde_Premio puntoVerde_Premio) {
        esperarConexion();
        String sql = "UPDATE punto_verde_premio SET idpuntoverde=?, idpremio=?, stock=? WHERE idpuntoverdepremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, puntoVerde_Premio.getIdPuntoVerde());
            statement.setInt(2, puntoVerde_Premio.getIdPremio());
            statement.setInt(3, puntoVerde_Premio.getStock());
            statement.setInt(4, puntoVerde_Premio.getIdPuntoVerdePremio());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar una relación PuntoVerde_Premio por IdPuntoVerdePremio
    public boolean borrarPuntoVerde_Premio(int idPuntoVerdePremio) {
        esperarConexion();
        String sql = "DELETE FROM punto_verde_premio WHERE idpuntoverdepremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPuntoVerdePremio);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer una relación PuntoVerde_Premio por IdPuntoVerdePremio
    public PuntoVerde_Premio traerPuntoVerde_PremioPorId(int idPuntoVerde, int idPremio) {
        esperarConexion();
        String sql = "SELECT * FROM punto_verde_premio WHERE idpuntoverde=? and idpremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPuntoVerde);
            statement.setInt(2, idPremio);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearPuntoVerde_PremioDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todas las relaciones PuntoVerde_Premio
    public List<PuntoVerde_Premio> traerTodasLasPuntosVerdes_Premios() {
        esperarConexion();
        List<PuntoVerde_Premio> puntosVerdes_Premios = new ArrayList<>();
        String sql = "SELECT * FROM punto_verde_premio";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                puntosVerdes_Premios.add(crearPuntoVerde_PremioDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puntosVerdes_Premios;
    }

    // Método auxiliar para crear un objeto PuntoVerde_Premio desde un ResultSet
    private PuntoVerde_Premio crearPuntoVerde_PremioDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idPuntoVerdePremio = resultSet.getInt("idpuntoverdepremio");
        int idPuntoVerde = resultSet.getInt("idpuntoverde");
        int idPremio = resultSet.getInt("idpremio");
        int stock = resultSet.getInt("stock");
        return new PuntoVerde_Premio(idPuntoVerdePremio, idPuntoVerde, idPremio, stock);
    }
}
