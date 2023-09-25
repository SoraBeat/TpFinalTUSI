package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UsuarioDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1); // Contador de espera

    public UsuarioDAO() {
        conectarBaseDeDatos();
    }
    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                UsuarioDAO.this.connection = connection;
                // La conexión se ha establecido correctamente
                connectionLatch.countDown(); // Indica que la conexión se ha establecido
            }

            @Override
            public void onConnectionFailure(String error) {
                // Ocurrió un error al establecer la conexión
                Log.e("UsuarioDAO", error);
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

    // Crear un nuevo usuario
    public boolean crearUsuario(Usuario usuario) {
        esperarConexion();
        String sql = "INSERT INTO usuarios (Alias, DNI, Email, Password, CantPuntos, FechaAlta, FechaBaja) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getAlias());
            statement.setString(2, usuario.getDni());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getPassword());
            statement.setInt(5, usuario.getCantPuntos());
            statement.setDate(6, new java.sql.Date(usuario.getFechaAlta().getTime()));
            statement.setDate(7, new java.sql.Date(usuario.getFechaBaja().getTime()));
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Editar un usuario existente
    public boolean editarUsuario(Usuario usuario) {
        esperarConexion();
        String sql = "UPDATE usuarios SET DNI=?, Email=?, Password=?, CantPuntos=?, FechaAlta=?, FechaBaja=? WHERE Alias=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getDni());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getPassword());
            statement.setInt(4, usuario.getCantPuntos());
            statement.setDate(5, new java.sql.Date(usuario.getFechaAlta().getTime()));
            statement.setDate(6, new java.sql.Date(usuario.getFechaBaja().getTime()));
            statement.setString(7, usuario.getAlias());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrar un usuario por Alias
    public boolean borrarUsuario(String alias) {
        esperarConexion();
        String sql = "DELETE FROM usuarios WHERE Alias=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, alias);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Traer un usuario por Alias
    public Usuario traerUsuarioPorAlias(String alias) {
        esperarConexion();
        String sql = "SELECT * FROM usuarios WHERE Alias=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, alias);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearUsuarioDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Traer todos los usuarios
    public List<Usuario> traerTodosLosUsuarios() {
        esperarConexion();
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                usuarios.add(crearUsuarioDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método auxiliar para crear un objeto Usuario desde un ResultSet
    private Usuario crearUsuarioDesdeResultSet(ResultSet resultSet) throws SQLException {
        String alias = resultSet.getString("Alias");
        String dni = resultSet.getString("DNI");
        String email = resultSet.getString("Email");
        String password = resultSet.getString("Password");
        int cantPuntos = resultSet.getInt("CantPuntos");
        Date fechaAlta = resultSet.getDate("FechaAlta");
        Date fechaBaja = resultSet.getDate("FechaBaja");
        return new Usuario(alias, dni, email, password, cantPuntos, fechaAlta, fechaBaja);
    }
}
