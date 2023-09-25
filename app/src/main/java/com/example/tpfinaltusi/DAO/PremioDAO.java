package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Premio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PremioDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public PremioDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                PremioDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("PremioDAO", error);
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

    // Crear un nuevo Premio
    public boolean crearPremio(Premio premio) {
        esperarConexion();
        String sql = "INSERT INTO premios (IdPremio, Nombre, Imagen, Precio) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, premio.getIdPremio());
            statement.setString(2, premio.getNombre());
            statement.setString(3, premio.getImagen());
            statement.setInt(4, premio.getPrecio());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un Premio existente
    public boolean editarPremio(Premio premio) {
        esperarConexion();
        String sql = "UPDATE premios SET Nombre=?, Imagen=?, Precio=? WHERE IdPremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, premio.getNombre());
            statement.setString(2, premio.getImagen());
            statement.setInt(3, premio.getPrecio());
            statement.setInt(4, premio.getIdPremio());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un Premio por IdPremio
    public boolean borrarPremio(int idPremio) {
        esperarConexion();
        String sql = "DELETE FROM premios WHERE IdPremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPremio);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un Premio por IdPremio
    public Premio traerPremioPorId(int idPremio) {
        esperarConexion();
        String sql = "SELECT * FROM premios WHERE IdPremio=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPremio);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearPremioDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todos los Premios
    public List<Premio> traerTodosLosPremios() {
        esperarConexion();
        List<Premio> premios = new ArrayList<>();
        String sql = "SELECT * FROM premios";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                premios.add(crearPremioDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return premios;
    }

    // Método auxiliar para crear un objeto Premio desde un ResultSet
    private Premio crearPremioDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idPremio = resultSet.getInt("IdPremio");
        String nombre = resultSet.getString("Nombre");
        String imagen = resultSet.getString("Imagen");
        int precio = resultSet.getInt("Precio");
        return new Premio(idPremio, nombre, imagen, precio);
    }
}
