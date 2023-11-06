package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.CanjeDAO;
import com.example.tpfinaltusi.entidades.Canje;

import java.util.List;

public class CanjeNegocio {

    private CanjeDAO canjeDAO;

    public CanjeNegocio() {
        canjeDAO = new CanjeDAO();
    }

    public void crearCanje(Canje canje, CanjeCallback callback) {
        // Realizar la operación de creación de Canje en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = canjeDAO.crearCanje(canje);
            if (resultado) {
                callback.onSuccess("Canje creado con éxito");
            } else {
                callback.onError("Error al crear Canje");
            }
        }).start();
    }

    public void editarCanje(Canje canje, CanjeCallback callback) {
        // Realizar la operación de edición de Canje en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = canjeDAO.editarCanje(canje);
            if (resultado) {
                callback.onSuccess("Canje editado con éxito");
            } else {
                callback.onError("Error al editar Canje");
            }
        }).start();
    }

    public void borrarCanje(int idCanje, CanjeCallback callback) {
        // Realizar la operación de borrado de Canje por IdCanje en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = canjeDAO.borrarCanje(idCanje);
            if (resultado) {
                callback.onSuccess("Canje borrado con éxito");
            } else {
                callback.onError("Error al borrar Canje");
            }
        }).start();
    }

    public void traerCanjePorId(int idCanje, CanjeCallback callback) {
        // Realizar la operación de traer Canje por IdCanje en un hilo o AsyncTask
        new Thread(() -> {
            Canje canje = canjeDAO.traerCanjePorId(idCanje);
            if (canje != null) {
                callback.onCanjeLoaded(canje);
            } else {
                callback.onError("Canje no encontrado");
            }
        }).start();
    }
    public void traerCanjePorIdUsuario(int idUsuario,CanjesCallback callback) {
        // Realizar la operación de traer todos los Canjes en un hilo o AsyncTask
        new Thread(() -> {
            List<Canje> canjes = canjeDAO.traerCanjePorIdUsuario(idUsuario);;
            callback.onCanjesLoaded(canjes);
        }).start();
    }
    public void traerTodosLosCanjes(CanjesCallback callback) {
        // Realizar la operación de traer todos los Canjes en un hilo o AsyncTask
        new Thread(() -> {
            List<Canje> canjes = canjeDAO.traerTodosLosCanjes();
            callback.onCanjesLoaded(canjes);
        }).start();
    }

    public interface CanjeCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onCanjeLoaded(Canje canje);
    }

    public interface CanjesCallback {
        void onCanjesLoaded(List<Canje> canjes);
    }
}
