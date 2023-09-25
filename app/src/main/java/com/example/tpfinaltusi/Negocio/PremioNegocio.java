package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.PremioDAO;
import com.example.tpfinaltusi.entidades.Premio;

import java.util.List;

public class PremioNegocio {

    private PremioDAO premioDAO;

    public PremioNegocio() {
        premioDAO = new PremioDAO();
    }

    public void crearPremio(Premio premio, PremioCallback callback) {
        // Realizar la operación de creación de Premio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = premioDAO.crearPremio(premio);
            if (resultado) {
                callback.onSuccess("Premio creado con éxito");
            } else {
                callback.onError("Error al crear Premio");
            }
        }).start();
    }

    public void editarPremio(Premio premio, PremioCallback callback) {
        // Realizar la operación de edición de Premio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = premioDAO.editarPremio(premio);
            if (resultado) {
                callback.onSuccess("Premio editado con éxito");
            } else {
                callback.onError("Error al editar Premio");
            }
        }).start();
    }

    public void borrarPremio(int idPremio, PremioCallback callback) {
        // Realizar la operación de borrado de Premio por IdPremio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = premioDAO.borrarPremio(idPremio);
            if (resultado) {
                callback.onSuccess("Premio borrado con éxito");
            } else {
                callback.onError("Error al borrar Premio");
            }
        }).start();
    }

    public void traerPremioPorId(int idPremio, PremioCallback callback) {
        // Realizar la operación de traer Premio por IdPremio en un hilo o AsyncTask
        new Thread(() -> {
            Premio premio = premioDAO.traerPremioPorId(idPremio);
            if (premio != null) {
                callback.onPremioLoaded(premio);
            } else {
                callback.onError("Premio no encontrado");
            }
        }).start();
    }

    public void traerTodosLosPremios(PremiosCallback callback) {
        // Realizar la operación de traer todos los Premios en un hilo o AsyncTask
        new Thread(() -> {
            List<Premio> premios = premioDAO.traerTodosLosPremios();
            callback.onPremiosLoaded(premios);
        }).start();
    }

    public interface PremioCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onPremioLoaded(Premio premio);
    }

    public interface PremiosCallback {
        void onPremiosLoaded(List<Premio> premios);
    }
}
