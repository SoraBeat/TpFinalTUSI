package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.PuntoVerde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PuntoVerdeDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public PuntoVerdeDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                PuntoVerdeDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("PuntoVerdeDAO", error);
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

    // Crear un nuevo punto verde
    public boolean crearPuntoVerde(PuntoVerde puntoVerde) {
        esperarConexion();
        String sql = "INSERT INTO puntos_verdes (idpuntoverde,idlocalidad, callealtura) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, puntoVerde.getIdPuntoVerde());
            statement.setInt(2, puntoVerde.getIdLocalidad());
            statement.setString(3, puntoVerde.getCalleAltura());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un punto verde existente
    public boolean editarPuntoVerde(PuntoVerde puntoVerde) {
        esperarConexion();
        String sql = "UPDATE punto_verde SET idlocalidad=?, callealtura=? WHERE idpuntoverde=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(2, puntoVerde.getIdLocalidad());
            statement.setString(3, puntoVerde.getCalleAltura());
            statement.setInt(4, puntoVerde.getIdPuntoVerde());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un punto verde por IdPuntoVerde
    public boolean borrarPuntoVerde(int idPuntoVerde) {
        esperarConexion();
        String sql = "DELETE FROM punto_verde WHERE idpuntoverde=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPuntoVerde);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un punto verde por IdPuntoVerde
    public PuntoVerde traerPuntoVerdePorId(int idPuntoVerde) {
        esperarConexion();
        String sql = "SELECT * FROM punto_verde WHERE idpuntoverde=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPuntoVerde);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearPuntoVerdeDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Traer todos los puntos verdes
    public List<PuntoVerde> traerTodosLosPuntosVerdes() {
        esperarConexion();
        List<PuntoVerde> puntosVerdes = new ArrayList<>();
        String sql = "SELECT * FROM punto_verde";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                puntosVerdes.add(crearPuntoVerdeDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puntosVerdes;
    }

    public List<PuntoVerde> traerTodosLosPuntosVerdesCS(int idPremio) {
        esperarConexion();
        List<PuntoVerde> puntosVerdes = new ArrayList<>();
        String sql = "SELECT a.* FROM punto_verde a join punto_verde_premio b on b.idpuntoverde = a.idpuntoverde and b.idpremio = ? and b.stock>0";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPremio);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                puntosVerdes.add(crearPuntoVerdeDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return puntosVerdes;
    }

    // Método auxiliar para crear un objeto PuntoVerde desde un ResultSet
    private PuntoVerde crearPuntoVerdeDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idPuntoVerde = resultSet.getInt("IdPuntoVerde");
        int idLocalidad = resultSet.getInt("IdLocalidad");
        String calleAltura = resultSet.getString("CalleAltura");
        return new PuntoVerde(idPuntoVerde, idLocalidad, calleAltura);
    }
}
