package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Canje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CanjeDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public CanjeDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                CanjeDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("CanjeDAO", error);
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

    // Crear un nuevo Canje
    public boolean crearCanje(Canje canje) {
        esperarConexion();
        String sql = "insert into canjes ( idusuario, idpremio, idpuntoverde, cantidad, precio,estado) values (?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, canje.getIdUsuario());
            statement.setInt(2, canje.getIdPremio());
            statement.setInt(3, canje.getIdPuntoVerde());
            statement.setInt(4, canje.getCantidad());
            statement.setInt(5, canje.getPrecio());
            statement.setBoolean(6, canje.isEstado());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un Canje existente
    public boolean editarCanje(Canje canje) {
        esperarConexion();
        String sql = "UPDATE canjes SET idusuario=?, idpremio=?, idpuntoverde=?, cantidad=?, precio=? WHERE idcanje=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, canje.getIdUsuario());
            statement.setInt(2, canje.getIdPremio());
            statement.setInt(3, canje.getIdPuntoVerde());
            statement.setInt(4, canje.getCantidad());
            statement.setInt(5, canje.getPrecio());
            statement.setInt(6, canje.getIdCanje());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un Canje por IdCanje
    public boolean borrarCanje(int idCanje) {
        esperarConexion();
        String sql = "DELETE FROM canjes WHERE idcanje=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCanje);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un Canje por IdCanje
    public Canje traerCanjePorId(int idCanje) {
        esperarConexion();
        String sql = "SELECT * FROM canjes WHERE idcanje=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCanje);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearCanjeDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Canje> traerCanjePorIdUsuario(int idUsuario) {
        esperarConexion();
        List<Canje> canjes = new ArrayList<>();
        String sql = "SELECT * FROM canjes WHERE idusuario=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                canjes.add(crearCanjeDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canjes;
    }
    // Traer todos los Canjes
    public List<Canje> traerTodosLosCanjes() {
        esperarConexion();
        List<Canje> canjes = new ArrayList<>();
        String sql = "SELECT * FROM canjes";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                canjes.add(crearCanjeDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canjes;
    }

    // Método auxiliar para crear un objeto Canje desde un ResultSet
    private Canje crearCanjeDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idCanje = resultSet.getInt("IdCanje");
        int idUsuario = resultSet.getInt("IdUsuario");
        int idPremio = resultSet.getInt("IdPremio");
        int idPuntoVerde = resultSet.getInt("IdPuntoVerde");
        int cantidad = resultSet.getInt("Cantidad");
        int precio = resultSet.getInt("Precio");
        boolean estado = resultSet.getBoolean("Estado");
        return new Canje(idCanje, idUsuario, idPremio, idPuntoVerde, cantidad, precio, estado);
    }
}

