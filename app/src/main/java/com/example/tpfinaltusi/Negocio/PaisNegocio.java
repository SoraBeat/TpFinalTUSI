package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.PaisDAO;
import com.example.tpfinaltusi.entidades.Pais;

import java.util.List;

public class PaisNegocio {

    private PaisDAO paisDAO;

    public PaisNegocio() {
        paisDAO = new PaisDAO();
    }

    public void crearPais(Pais pais, PaisCallback callback) {
        // Realizar la operación de creación de País en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = paisDAO.crearPais(pais);
            if (resultado) {
                callback.onSuccess("País creado con éxito");
            } else {
                callback.onError("Error al crear País");
            }
        }).start();
    }

    public void editarPais(Pais pais, PaisCallback callback) {
        // Realizar la operación de edición de País en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = paisDAO.editarPais(pais);
            if (resultado) {
                callback.onSuccess("País editado con éxito");
            } else {
                callback.onError("Error al editar País");
            }
        }).start();
    }

    public void borrarPais(int idPais, PaisCallback callback) {
        // Realizar la operación de borrado de País por IdPais en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = paisDAO.borrarPais(idPais);
            if (resultado) {
                callback.onSuccess("País borrado con éxito");
            } else {
                callback.onError("Error al borrar País");
            }
        }).start();
    }

    public void traerPaisPorId(int idPais, PaisCallback callback) {
        // Realizar la operación de traer País por IdPais en un hilo o AsyncTask
        new Thread(() -> {
            Pais pais = paisDAO.traerPaisPorId(idPais);
            if (pais != null) {
                callback.onPaisLoaded(pais);
            } else {
                callback.onError("País no encontrado");
            }
        }).start();
    }

    public void traerTodosLosPaises(PaisesCallback callback) {
        // Realizar la operación de traer todos los Países en un hilo o AsyncTask
        new Thread(() -> {
            List<Pais> paises = paisDAO.traerTodosLosPaises();
            callback.onPaisesLoaded(paises);
        }).start();
    }

    public interface PaisCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onPaisLoaded(Pais pais);
    }

    public interface PaisesCallback {
        void onPaisesLoaded(List<Pais> paises);
    }
}
