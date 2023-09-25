package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Provincia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ProvinciaDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public ProvinciaDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                ProvinciaDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("ProvinciaDAO", error);
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

    // Crear una nueva provincia
    public boolean crearProvincia(Provincia provincia) {
        esperarConexion();
        String sql = "INSERT INTO provincias (IDProvincia, IdPais, Nombre) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, provincia.getIdProvincia());
            statement.setInt(2, provincia.getIdPais());
            statement.setString(3, provincia.getNombre());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una provincia existente
    public boolean editarProvincia(Provincia provincia) {
        esperarConexion();
        String sql = "UPDATE provincias SET IdPais=?, Nombre=? WHERE IDProvincia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, provincia.getIdPais());
            statement.setString(2, provincia.getNombre());
            statement.setInt(3, provincia.getIdProvincia());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar una provincia por IDProvincia
    public boolean borrarProvincia(int idProvincia) {
        esperarConexion();
        String sql = "DELETE FROM provincias WHERE IDProvincia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProvincia);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer una provincia por IDProvincia
    public Provincia traerProvinciaPorId(int idProvincia) {
        esperarConexion();
        String sql = "SELECT * FROM provincias WHERE IDProvincia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProvincia);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearProvinciaDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todas las provincias
    public List<Provincia> traerTodasLasProvincias() {
        esperarConexion();
        List<Provincia> provincias = new ArrayList<>();
        String sql = "SELECT * FROM provincias";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                provincias.add(crearProvinciaDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return provincias;
    }

    // Método auxiliar para crear un objeto Provincia desde un ResultSet
    private Provincia crearProvinciaDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idProvincia = resultSet.getInt("IDProvincia");
        int idPais = resultSet.getInt("IdPais");
        String nombre = resultSet.getString("Nombre");
        return new Provincia(idProvincia, idPais, nombre);
    }
}
