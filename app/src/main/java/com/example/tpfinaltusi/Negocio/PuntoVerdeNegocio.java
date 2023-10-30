package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.PuntoVerdeDAO;
import com.example.tpfinaltusi.entidades.PuntoVerde;

import java.util.List;

public class PuntoVerdeNegocio {

    private PuntoVerdeDAO puntoVerdeDAO;

    public PuntoVerdeNegocio() {
        puntoVerdeDAO = new PuntoVerdeDAO();
    }

    public void crearPuntoVerde(PuntoVerde puntoVerde, PuntoVerdeCallback callback) {
        // Realizar la operación de creación de PuntoVerde en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerdeDAO.crearPuntoVerde(puntoVerde);
            if (resultado) {
                callback.onSuccess("Punto Verde creado con éxito");
            } else {
                callback.onError("Error al crear Punto Verde");
            }
        }).start();
    }

    public void editarPuntoVerde(PuntoVerde puntoVerde, PuntoVerdeCallback callback) {
        // Realizar la operación de edición de PuntoVerde en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerdeDAO.editarPuntoVerde(puntoVerde);
            if (resultado) {
                callback.onSuccess("Punto Verde editado con éxito");
            } else {
                callback.onError("Error al editar Punto Verde");
            }
        }).start();
    }

    public void borrarPuntoVerde(int idPuntoVerde, PuntoVerdeCallback callback) {
        // Realizar la operación de borrado de PuntoVerde por IdPuntoVerde en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerdeDAO.borrarPuntoVerde(idPuntoVerde);
            if (resultado) {
                callback.onSuccess("Punto Verde borrado con éxito");
            } else {
                callback.onError("Error al borrar Punto Verde");
            }
        }).start();
    }

    public void traerPuntoVerdePorId(int idPuntoVerde, PuntoVerdeCallback callback) {
        // Realizar la operación de traer PuntoVerde por IdPuntoVerde en un hilo o AsyncTask
        new Thread(() -> {
            PuntoVerde puntoVerde = puntoVerdeDAO.traerPuntoVerdePorId(idPuntoVerde);
            if (puntoVerde != null) {
                callback.onPuntoVerdeLoaded(puntoVerde);
            } else {
                callback.onError("Punto Verde no encontrado");
            }
        }).start();
    }

    public void traerTodosLosPuntosVerdes(PuntoVerdesCallback callback) {
        // Realizar la operación de traer todos los Puntos Verdes en un hilo o AsyncTask
        new Thread(() -> {
            List<PuntoVerde> puntosVerdes = puntoVerdeDAO.traerTodosLosPuntosVerdes();
            callback.onPuntoVerdesLoaded(puntosVerdes);
        }).start();
    }

    public void traerTodosLosPuntosVerdesCS(PuntoVerdesCallback callback) {
        // Realizar la operación de traer todos los Puntos Verdes en un hilo o AsyncTask
        new Thread(() -> {
            List<PuntoVerde> puntosVerdes = puntoVerdeDAO.traerTodosLosPuntosVerdesCS();
            callback.onPuntoVerdesLoaded(puntosVerdes);
        }).start();
    }

    public interface PuntoVerdeCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onPuntoVerdeLoaded(PuntoVerde puntoVerde);
    }

    public interface PuntoVerdesCallback {
        void onPuntoVerdesLoaded(List<PuntoVerde> puntosVerdes);
    }
}
