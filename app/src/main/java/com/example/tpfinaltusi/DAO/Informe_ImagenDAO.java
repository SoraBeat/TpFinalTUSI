package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Informe_Imagen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Informe_ImagenDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public Informe_ImagenDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                Informe_ImagenDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("Informe_ImagenDAO", error);
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

    // Crear una nueva relación Informe_Imagen
    public boolean crearInforme_Imagen(Informe_Imagen informe_Imagen) {
        esperarConexion();
        String sql = "INSERT INTO informe_imagen (IdInforme, Imagen) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, informe_Imagen.getIdInforme());
            statement.setString(2, informe_Imagen.getImagen());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una relación Informe_Imagen existente
    public boolean editarInforme_Imagen(Informe_Imagen informe_Imagen) {
        esperarConexion();
        String sql = "UPDATE informe_imagen SET IdInforme=?, Imagen=? WHERE IdInformeImagen=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, informe_Imagen.getIdInforme());
            statement.setString(2, informe_Imagen.getImagen());
            statement.setInt(3, informe_Imagen.getIdInformeImagen());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar una relación Informe_Imagen por IdInformeImagen
    public boolean borrarInforme_Imagen(int idInformeImagen) {
        esperarConexion();
        String sql = "DELETE FROM informe_imagen WHERE IdInformeImagen=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInformeImagen);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer una relación Informe_Imagen por IdInformeImagen
    public Informe_Imagen traerInforme_ImagenPorId(int idInformeImagen) {
        esperarConexion();
        String sql = "SELECT * FROM informe_imagen WHERE IdInformeImagen=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idInformeImagen);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearInforme_ImagenDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todas las relaciones Informe_Imagen
    public List<Informe_Imagen> traerTodasLasInformes_Imagenes() {
        esperarConexion();
        List<Informe_Imagen> informes_Imagenes = new ArrayList<>();
        String sql = "SELECT * FROM informe_imagen";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                informes_Imagenes.add(crearInforme_ImagenDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return informes_Imagenes;
    }

    // Método auxiliar para crear un objeto Informe_Imagen desde un ResultSet
    private Informe_Imagen crearInforme_ImagenDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idInformeImagen = resultSet.getInt("IdInformeImagen");
        int idInforme = resultSet.getInt("IdInforme");
        String imagen = resultSet.getString("Imagen");
        return new Informe_Imagen(idInformeImagen, idInforme, imagen);
    }
}
