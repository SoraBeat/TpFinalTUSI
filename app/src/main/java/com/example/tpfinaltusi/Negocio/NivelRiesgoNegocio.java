package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.NivelRiesgoDAO;
import com.example.tpfinaltusi.entidades.NivelRiesgo;

import java.util.List;

public class NivelRiesgoNegocio {

    private NivelRiesgoDAO nivelRiesgoDAO;

    public NivelRiesgoNegocio() {
        nivelRiesgoDAO = new NivelRiesgoDAO();
    }

    public void crearNivelRiesgo(NivelRiesgo nivelRiesgo, NivelRiesgoCallback callback) {
        // Realizar la operación de creación de Nivel de Riesgo en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = nivelRiesgoDAO.crearNivelRiesgo(nivelRiesgo);
            if (resultado) {
                callback.onSuccess("Nivel de Riesgo creado con éxito");
            } else {
                callback.onError("Error al crear Nivel de Riesgo");
            }
        }).start();
    }

    public void editarNivelRiesgo(NivelRiesgo nivelRiesgo, NivelRiesgoCallback callback) {
        // Realizar la operación de edición de Nivel de Riesgo en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = nivelRiesgoDAO.editarNivelRiesgo(nivelRiesgo);
            if (resultado) {
                callback.onSuccess("Nivel de Riesgo editado con éxito");
            } else {
                callback.onError("Error al editar Nivel de Riesgo");
            }
        }).start();
    }

    public void borrarNivelRiesgo(int idNivel, NivelRiesgoCallback callback) {
        // Realizar la operación de borrado de Nivel de Riesgo por IdNivel en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = nivelRiesgoDAO.borrarNivelRiesgo(idNivel);
            if (resultado) {
                callback.onSuccess("Nivel de Riesgo borrado con éxito");
            } else {
                callback.onError("Error al borrar Nivel de Riesgo");
            }
        }).start();
    }

    public void traerNivelRiesgoPorId(int idNivel, NivelRiesgoCallback callback) {
        // Realizar la operación de traer Nivel de Riesgo por IdNivel en un hilo o AsyncTask
        new Thread(() -> {
            NivelRiesgo nivelRiesgo = nivelRiesgoDAO.traerNivelRiesgoPorId(idNivel);
            if (nivelRiesgo != null) {
                callback.onNivelRiesgoLoaded(nivelRiesgo);
            } else {
                callback.onError("Nivel de Riesgo no encontrado");
            }
        }).start();
    }

    public void traerTodosLosNivelesRiesgo(NivelesRiesgoCallback callback) {
        // Realizar la operación de traer todos los Niveles de Riesgo en un hilo o AsyncTask
        new Thread(() -> {
            List<NivelRiesgo> nivelesRiesgo = nivelRiesgoDAO.traerTodosLosNivelesRiesgo();
            callback.onNivelesRiesgoLoaded(nivelesRiesgo);
        }).start();
    }

    public interface NivelRiesgoCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onNivelRiesgoLoaded(NivelRiesgo nivelRiesgo);
    }

    public interface NivelesRiesgoCallback {
        void onNivelesRiesgoLoaded(List<NivelRiesgo> nivelesRiesgo);
    }
}
