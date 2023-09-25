package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Localidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LocalidadDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public LocalidadDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                LocalidadDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("LocalidadDAO", error);
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

    // Crear una nueva localidad
    public boolean crearLocalidad(Localidad localidad) {
        esperarConexion();
        String sql = "INSERT INTO localidades (IdProvincia, IdLocalidad, Nombre) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, localidad.getIdProvincia());
            statement.setInt(2, localidad.getIdLocalidad());
            statement.setString(3, localidad.getNombre());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una localidad existente
    public boolean editarLocalidad(Localidad localidad) {
        esperarConexion();
        String sql = "UPDATE localidades SET Nombre=? WHERE IdProvincia=? AND IdLocalidad=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, localidad.getNombre());
            statement.setInt(2, localidad.getIdProvincia());
            statement.setInt(3, localidad.getIdLocalidad());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar una localidad por IdProvincia e IdLocalidad
    public boolean borrarLocalidad(int idProvincia, int idLocalidad) {
        esperarConexion();
        String sql = "DELETE FROM localidades WHERE IdProvincia=? AND IdLocalidad=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProvincia);
            statement.setInt(2, idLocalidad);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer una localidad por IdProvincia e IdLocalidad
    public Localidad traerLocalidadPorIds(int idProvincia, int idLocalidad) {
        esperarConexion();
        String sql = "SELECT * FROM localidades WHERE IdProvincia=? AND IdLocalidad=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProvincia);
            statement.setInt(2, idLocalidad);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearLocalidadDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todas las localidades de una provincia
    public List<Localidad> traerLocalidadesPorProvincia(int idProvincia) {
        esperarConexion();
        List<Localidad> localidades = new ArrayList<>();
        String sql = "SELECT * FROM localidades WHERE IdProvincia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProvincia);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                localidades.add(crearLocalidadDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localidades;
    }

    // Método auxiliar para crear un objeto Localidad desde un ResultSet
    private Localidad crearLocalidadDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idProvincia = resultSet.getInt("IdProvincia");
        int idLocalidad = resultSet.getInt("IdLocalidad");
        String nombre = resultSet.getString("Nombre");
        return new Localidad(idProvincia, idLocalidad, nombre);
    }
}
