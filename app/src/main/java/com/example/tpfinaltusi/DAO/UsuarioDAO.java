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
        if (correoElectronicoExiste(usuario.getEmail())) {
            // El correo electrónico ya está en uso, no se puede crear el usuario
            return false;
        }
        String sql = "INSERT INTO usuarios (alias, dni, email, password, cantpuntos, fechaalta, fechabaja,puntostotalesobtenidos,esadmin,imagen) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getAlias());
            statement.setString(2, usuario.getDni());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getPassword());
            statement.setInt(5, usuario.getCantPuntos());
            statement.setDate(6, new java.sql.Date(usuario.getFechaAlta().getTime()));
            statement.setDate(7, new java.sql.Date(usuario.getFechaBaja().getTime()));
            statement.setInt(8, usuario.getPuntosTotalesObtenidos());
            statement.setBoolean(9,usuario.isEsAdmin());
            statement.setString(10,usuario.getImagen());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean correoElectronicoExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si count es mayor que 0, el correo electrónico ya existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Editar un usuario existente
    public boolean editarUsuario(Usuario usuario) {
        esperarConexion();
        String sql = "UPDATE usuarios SET dni=?, email=?, password=?, cantpuntos=?, fechaalta=?, fechabaja=?, puntostotalesobtenidos=?, esadmin=?, imagen=? WHERE alias=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, usuario.getDni());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getPassword());
            statement.setInt(4, usuario.getCantPuntos());
            statement.setDate(5, new java.sql.Date(usuario.getFechaAlta().getTime()));
            statement.setDate(6, new java.sql.Date(usuario.getFechaBaja().getTime()));
            statement.setInt(7, usuario.getPuntosTotalesObtenidos());
            statement.setBoolean(8,usuario.isEsAdmin());
            statement.setString(8,usuario.getImagen());
            statement.setString(10, usuario.getAlias());
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
        String sql = "DELETE FROM usuarios WHERE alias=?";
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
        String sql = "SELECT * FROM usuarios WHERE alias=?";
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
        int id = resultSet.getInt("idusuario");
        String alias = resultSet.getString("alias");
        String dni = resultSet.getString("dni");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        int cantPuntos = resultSet.getInt("cantpuntos");
        Date fechaAlta = resultSet.getDate("fechaalta");
        Date fechaBaja = resultSet.getDate("fechabaja");
        int cantPuntosTotales = resultSet.getInt("puntostotalesobtenidos");
        boolean esAdmin = resultSet.getBoolean("esadmin");
        String imagen = resultSet.getString("imagen");
        return new Usuario(id,alias, dni, email, password, cantPuntos, fechaAlta, fechaBaja,cantPuntosTotales,esAdmin,imagen);
    }
    // Verificar las credenciales del usuario
    public boolean verificarCredenciales(String email, String password) {
        esperarConexion();
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si count es mayor que 0, las credenciales son válidas
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Usuario traerUsuarioPorEmail(String email) {
        esperarConexion();
        String sql = "SELECT * FROM usuarios WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearUsuarioDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario traerUsuarioPorId(int idUsuario) {
        esperarConexion();
        String sql = "SELECT * FROM usuarios WHERE idusuario=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearUsuarioDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Sumar puntos a la cuenta de un usuario por su ID
    public boolean sumarPuntosAUsuario(int idUsuario, int puntosASumar) {
        esperarConexion();
        String sql = "UPDATE usuarios SET cantpuntos = cantpuntos + ?, puntostotalesobtenidos = puntostotalesobtenidos + ? WHERE idusuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, puntosASumar);
            statement.setInt(2, puntosASumar);
            statement.setInt(3, idUsuario);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean restarPuntosAUsuario(int idUsuario, int puntosARestar) {
        esperarConexion();
        String sql = "UPDATE usuarios SET cantpuntos = cantpuntos - ? WHERE idusuario = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, puntosARestar);
            statement.setInt(2, idUsuario);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // En la clase UsuarioDAO
    public String obtenerPasswordPorEmail(String email) {
        esperarConexion();
        String sql = "SELECT password FROM usuarios WHERE email=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
