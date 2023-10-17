package com.example.tpfinaltusi.Negocio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.tpfinaltusi.DAO.UsuarioDAO;
import com.example.tpfinaltusi.entidades.Usuario;

import java.util.List;

public class UsuarioNegocio {

    private static UsuarioDAO usuarioDAO = new UsuarioDAO();

    public UsuarioNegocio() {

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
    public boolean crearUsuario(Usuario usuario) {
        // Realizar la operación de creación de usuario en un hilo o AsyncTask
        boolean resultado = usuarioDAO.crearUsuario(usuario);
        return resultado;
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
    public void buscarUsuarioPorCredenciales(Context context, String email, String password, UsuarioCallback callback) {
        // Realizar la operación de búsqueda de usuario por credenciales en un hilo o AsyncTask
        new Thread(() -> {
            Usuario usuario;
            boolean loginExitoso = login(email, password);
            if (loginExitoso) {
                usuario = usuarioDAO.traerUsuarioPorEmail(email);
            } else {
                usuario = null;
            }

            // Invocar el callback con el resultado en el hilo principal
            ((Activity)context).runOnUiThread(() -> {
                if (usuario != null) {
                    callback.onUsuarioLoaded(usuario);
                } else {
                    callback.onError("Credenciales incorrectas");
                }
            });
        }).start();
    }

    public static void guardarIDUsuario(Context context, int idUsuario) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UsuarioGuardado", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idUsuario", idUsuario);
        editor.apply();
    }
    public static int obtenerIDUsuario(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UsuarioGuardado", Context.MODE_PRIVATE);
        int idUsuario = sharedPreferences.getInt("idUsuario",-1);
        return  idUsuario;
    }
    public void buscarUsuarioPorId(int idUsuario, UsuarioCallback callback) {
        // Realizar la operación de búsqueda de usuario por ID en un hilo o AsyncTask
        new Thread(() -> {
            Usuario usuario = usuarioDAO.traerUsuarioPorId(idUsuario);
            if (usuario != null) {
                callback.onUsuarioLoaded(usuario);
            } else {
                callback.onError("Usuario no encontrado");
            }
        }).start();
    }
    public void sumarPuntosAUsuarioPorId(int idUsuario, int puntosASumar, UsuarioCallback callback) {
        // Realizar la operación de sumar puntos a un usuario por su ID en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = usuarioDAO.sumarPuntosAUsuario(idUsuario, puntosASumar);
            if (resultado) {
                callback.onSuccess("Puntos sumados con éxito");
            } else {
                callback.onError("Error al sumar puntos");
            }
        }).start();
    }
    public void restarPuntosAUsuarioPorId(int idUsuario, int puntosARestar,UsuarioCallback callback) {
        // Realizar la operación de sumar puntos a un usuario por su ID en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = usuarioDAO.restarPuntosAUsuario(idUsuario, puntosARestar);
            if (resultado) {
                callback.onSuccess("Puntos restados con éxito");
            } else {
                callback.onError("Error al restados puntos");
            }
        }).start();
    }
    public void obtenerContraseñaPorEmail(String email, ContraseñaCallback callback) {
        // Realizar la operación de obtener contraseña por email en un hilo o AsyncTask
        new Thread(() -> {
            String contraseña = usuarioDAO.obtenerPasswordPorEmail(email);
            if (contraseña != null) {
                callback.onContraseñaObtenida(contraseña);
            } else {
                callback.onError("Correo electrónico no encontrado");
            }
        }).start();
    }

    public interface ContraseñaCallback {
        void onContraseñaObtenida(String contraseña);

        void onError(String error);
    }

}
