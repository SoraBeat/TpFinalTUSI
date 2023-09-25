package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Pais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PaisDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public PaisDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                PaisDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("PaisDAO", error);
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

    // Crear un nuevo país
    public boolean crearPais(Pais pais) {
        esperarConexion();
        String sql = "INSERT INTO paises (IdPais, Nombre) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, pais.getIdPais());
            statement.setString(2, pais.getNombre());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un país existente
    public boolean editarPais(Pais pais) {
        esperarConexion();
        String sql = "UPDATE paises SET Nombre=? WHERE IdPais=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pais.getNombre());
            statement.setInt(2, pais.getIdPais());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un país por IdPais
    public boolean borrarPais(int idPais) {
        esperarConexion();
        String sql = "DELETE FROM paises WHERE IdPais=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPais);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un país por IdPais
    public Pais traerPaisPorId(int idPais) {
        esperarConexion();
        String sql = "SELECT * FROM paises WHERE IdPais=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPais);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearPaisDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todos los países
    public List<Pais> traerTodosLosPaises() {
        esperarConexion();
        List<Pais> paises = new ArrayList<>();
        String sql = "SELECT * FROM paises";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                paises.add(crearPaisDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paises;
    }

    // Método auxiliar para crear un objeto Pais desde un ResultSet
    private Pais crearPaisDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idPais = resultSet.getInt("IdPais");
        String nombre = resultSet.getString("Nombre");
        return new Pais(idPais, nombre);
    }
}
