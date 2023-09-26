package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Noticia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NoticiaDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public NoticiaDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                NoticiaDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("NoticiaDAO", error);
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

    // Crear una nueva Noticia
    public boolean crearNoticia(Noticia noticia) {
        esperarConexion();
        String sql = "INSERT INTO noticias (IdNoticia, Titulo, Cuerpo, Categoria, Imagen, FechaAlta, IdLocalidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, noticia.getIdNoticia());
            statement.setString(2, noticia.getTitulo());
            statement.setString(3, noticia.getCuerpo());
            statement.setString(4, noticia.getCategoria());
            statement.setString(5, noticia.getImagen());
            statement.setDate(6, new java.sql.Date(noticia.getFechaAlta().getTime()));
            statement.setInt(7, noticia.getIdLocalidad());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar una Noticia existente
    public boolean editarNoticia(Noticia noticia) {
        esperarConexion();
        String sql = "UPDATE noticias SET Titulo=?, Cuerpo=?, Categoria=?, Imagen=?, FechaAlta=?, IdLocalidad=? WHERE IdNoticia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, noticia.getTitulo());
            statement.setString(2, noticia.getCuerpo());
            statement.setString(3, noticia.getCategoria());
            statement.setString(4, noticia.getImagen());
            statement.setDate(5, new java.sql.Date(noticia.getFechaAlta().getTime()));
            statement.setInt(6, noticia.getIdLocalidad());
            statement.setInt(7, noticia.getIdNoticia());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar una Noticia por IdNoticia
    public boolean borrarNoticia(int idNoticia) {
        esperarConexion();
        String sql = "DELETE FROM noticias WHERE IdNoticia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idNoticia);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer una Noticia por IdNoticia
    public Noticia traerNoticiaPorId(int idNoticia) {
        esperarConexion();
        String sql = "SELECT * FROM noticias WHERE IdNoticia=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idNoticia);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearNoticiaDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todas las Noticias
    public List<Noticia> traerTodasLasNoticias() {
        esperarConexion();
        List<Noticia> noticias = new ArrayList<>();
        String sql = "SELECT * FROM noticias";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                noticias.add(crearNoticiaDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noticias;
    }

    // Método auxiliar para crear un objeto Noticia desde un ResultSet
    private Noticia crearNoticiaDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idNoticia = resultSet.getInt("idnoticia");
        String titulo = resultSet.getString("titulo");
        String cuerpo = resultSet.getString("cuerpo");
        String categoria = resultSet.getString("categoria");
        String imagen = resultSet.getString("imagen");
        Date fechaAlta = resultSet.getDate("fechaalta");
        int idLocalidad = resultSet.getInt("idlocalidad");
        return new Noticia(idNoticia, titulo, cuerpo, categoria, imagen, fechaAlta, idLocalidad);
    }

}

