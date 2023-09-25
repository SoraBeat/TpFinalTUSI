package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.UsuarioDAO;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.List;

public class UsuarioNegocio {

    private static UsuarioDAO usuarioDAO;

    public UsuarioNegocio() {
        usuarioDAO = new UsuarioDAO();
    }

    public void crearUsuario(Usuario usuario, UsuarioCallback callback) {
        // Realizar la operación de creación de usuario en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = usuarioDAO.crearUsuario(usuario);
            if (resultado) {
                callback.onSuccess("Usuario creado con éxito");
            } else {
                callback.onError("Error al crear usuario");
            }
        }).start();
    }

    public void editarUsuario(Usuario usuario, UsuarioCallback callback) {
        // Realizar la operación de edición de usuario en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = usuarioDAO.editarUsuario(usuario);
            if (resultado) {
                callback.onSuccess("Usuario editado con éxito");
            } else {
                callback.onError("Error al editar usuario");
            }
        }).start();
    }

    public void borrarUsuario(String alias, UsuarioCallback callback) {
        // Realizar la operación de borrado de usuario en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = usuarioDAO.borrarUsuario(alias);
            if (resultado) {
                callback.onSuccess("Usuario borrado con éxito");
            } else {
                callback.onError("Error al borrar usuario");
            }
        }).start();
    }

    public void traerUsuarioPorAlias(String alias, UsuarioCallback callback) {
        // Realizar la operación de traer usuario por alias en un hilo o AsyncTask
        new Thread(() -> {
            Usuario usuario = usuarioDAO.traerUsuarioPorAlias(alias);
            if (usuario != null) {
                callback.onUsuarioLoaded(usuario);
            } else {
                callback.onError("Usuario no encontrado");
            }
        }).start();
    }

    public void traerTodosLosUsuarios(UsuariosCallback callback) {
        // Realizar la operación de traer todos los usuarios en un hilo o AsyncTask
        new Thread(() -> {
            List<Usuario> usuarios = usuarioDAO.traerTodosLosUsuarios();
            callback.onUsuariosLoaded(usuarios);
        }).start();
    }

    public interface UsuarioCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onUsuarioLoaded(Usuario usuario);
    }
    public static boolean login(String email, String password) {
        return usuarioDAO.verificarCredenciales(email, password);
    }

    public interface UsuariosCallback {
        void onUsuariosLoaded(List<Usuario> usuarios);
    }
}
