package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.Informe_ImagenDAO;
import com.example.tpfinaltusi.entidades.Informe_Imagen;

import java.util.List;

public class InformeImagenNegocio {

    private Informe_ImagenDAO informeImagenDAO;

    public InformeImagenNegocio() {
        informeImagenDAO = new Informe_ImagenDAO();
    }

    public void crearInformeImagen(Informe_Imagen informeImagen, InformeImagenCallback callback) {
        // Realizar la operación de creación de Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeImagenDAO.crearInforme_Imagen(informeImagen);
            if (resultado) {
                callback.onSuccess("Informe_Imagen creado con éxito");
            } else {
                callback.onError("Error al crear Informe_Imagen");
            }
        }).start();
    }

    public void editarInformeImagen(Informe_Imagen informeImagen, InformeImagenCallback callback) {
        // Realizar la operación de edición de Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeImagenDAO.editarInforme_Imagen(informeImagen);
            if (resultado) {
                callback.onSuccess("Informe_Imagen editado con éxito");
            } else {
                callback.onError("Error al editar Informe_Imagen");
            }
        }).start();
    }

    public void borrarInformeImagen(int idInformeImagen, InformeImagenCallback callback) {
        // Realizar la operación de borrado de Informe_Imagen por IdInformeImagen en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeImagenDAO.borrarInforme_Imagen(idInformeImagen);
            if (resultado) {
                callback.onSuccess("Informe_Imagen borrado con éxito");
            } else {
                callback.onError("Error al borrar Informe_Imagen");
            }
        }).start();
    }

    public void traerInformeImagenPorId(int idInformeImagen, InformeImagenCallback callback) {
        // Realizar la operación de traer Informe_Imagen por IdInformeImagen en un hilo o AsyncTask
        new Thread(() -> {
            Informe_Imagen informeImagen = informeImagenDAO.traerInforme_ImagenPorId(idInformeImagen);
            if (informeImagen != null) {
                callback.onInformeImagenLoaded(informeImagen);
            } else {
                callback.onError("Informe_Imagen no encontrado");
            }
        }).start();
    }

    public void traerTodasLasInformeImagenes(InformeImagenesCallback callback) {
        // Realizar la operación de traer todas las Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            List<Informe_Imagen> informeImagenes = informeImagenDAO.traerTodasLasInformes_Imagenes();
            callback.onInformeImagenesLoaded(informeImagenes);
        }).start();
    }

    public interface InformeImagenCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onInformeImagenLoaded(Informe_Imagen informeImagen);
    }

    public interface InformeImagenesCallback {
        void onInformeImagenesLoaded(List<Informe_Imagen> informeImagenes);
    }
}
