package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.NivelRiesgo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NivelRiesgoDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public NivelRiesgoDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                NivelRiesgoDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("NivelRiesgoDAO", error);
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

    // Crear un nuevo nivel de riesgo
    public boolean crearNivelRiesgo(NivelRiesgo nivelRiesgo) {
        esperarConexion();
        String sql = "INSERT INTO niveles_riesgo (IdNivel, Nombre) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nivelRiesgo.getIdNivel());
            statement.setString(2, nivelRiesgo.getNombre());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un nivel de riesgo existente
    public boolean editarNivelRiesgo(NivelRiesgo nivelRiesgo) {
        esperarConexion();
        String sql = "UPDATE niveles_riesgo SET Nombre=? WHERE IdNivel=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nivelRiesgo.getNombre());
            statement.setInt(2, nivelRiesgo.getIdNivel());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un nivel de riesgo por IdNivel
    public boolean borrarNivelRiesgo(int idNivel) {
        esperarConexion();
        String sql = "DELETE FROM niveles_riesgo WHERE IdNivel=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idNivel);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un nivel de riesgo por IdNivel
    public NivelRiesgo traerNivelRiesgoPorId(int idNivel) {
        esperarConexion();
        String sql = "SELECT * FROM niveles_riesgo WHERE IdNivel=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idNivel);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearNivelRiesgoDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todos los niveles de riesgo
    public List<NivelRiesgo> traerTodosLosNivelesRiesgo() {
        esperarConexion();
        List<NivelRiesgo> nivelesRiesgo = new ArrayList<>();
        String sql = "SELECT * FROM niveles_riesgo";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                nivelesRiesgo.add(crearNivelRiesgoDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nivelesRiesgo;
    }

    // Método auxiliar para crear un objeto NivelRiesgo desde un ResultSet
    private NivelRiesgo crearNivelRiesgoDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idNivel = resultSet.getInt("IdNivel");
        String nombre = resultSet.getString("Nombre");
        return new NivelRiesgo(idNivel, nombre);
    }
}
