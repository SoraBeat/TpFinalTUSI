package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.NoticiaDAO;
import com.example.tpfinaltusi.entidades.Noticia;

import java.util.List;

public class NoticiaNegocio {

    private NoticiaDAO noticiaDAO;

    public NoticiaNegocio() {
        noticiaDAO = new NoticiaDAO();
    }

    public void crearNoticia(Noticia noticia, NoticiaCallback callback) {
        // Realizar la operación de creación de Noticia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = noticiaDAO.crearNoticia(noticia);
            if (resultado) {
                callback.onSuccess("Noticia creada con éxito");
            } else {
                callback.onError("Error al crear Noticia");
            }
        }).start();
    }

    public void editarNoticia(Noticia noticia, NoticiaCallback callback) {
        // Realizar la operación de edición de Noticia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = noticiaDAO.editarNoticia(noticia);
            if (resultado) {
                callback.onSuccess("Noticia editada con éxito");
            } else {
                callback.onError("Error al editar Noticia");
            }
        }).start();
    }

    public void borrarNoticia(int idNoticia, NoticiaCallback callback) {
        // Realizar la operación de borrado de Noticia por IdNoticia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = noticiaDAO.borrarNoticia(idNoticia);
            if (resultado) {
                callback.onSuccess("Noticia borrada con éxito");
            } else {
                callback.onError("Error al borrar Noticia");
            }
        }).start();
    }

    public void traerNoticiaPorId(int idNoticia, NoticiaCallback callback) {
        // Realizar la operación de traer Noticia por IdNoticia en un hilo o AsyncTask
        new Thread(() -> {
            Noticia noticia = noticiaDAO.traerNoticiaPorId(idNoticia);
            if (noticia != null) {
                callback.onNoticiaLoaded(noticia);
            } else {
                callback.onError("Noticia no encontrada");
            }
        }).start();
    }

    public void traerTodasLasNoticias(NoticiasCallback callback) {
        // Realizar la operación de traer todas las Noticias en un hilo o AsyncTask
        new Thread(() -> {
            List<Noticia> noticias = noticiaDAO.traerTodasLasNoticias();
            callback.onNoticiasLoaded(noticias);
        }).start();
    }

    public interface NoticiaCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onNoticiaLoaded(Noticia noticia);
    }

    public interface NoticiasCallback {
        void onNoticiasLoaded(List<Noticia> noticias);
    }
}
