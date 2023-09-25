package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.ProvinciaDAO;
import com.example.tpfinaltusi.entidades.Provincia;

import java.util.List;

public class ProvinciaNegocio {

    private ProvinciaDAO provinciaDAO;

    public ProvinciaNegocio() {
        provinciaDAO = new ProvinciaDAO();
    }

    public void crearProvincia(Provincia provincia, ProvinciaCallback callback) {
        // Realizar la operación de creación de Provincia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = provinciaDAO.crearProvincia(provincia);
            if (resultado) {
                callback.onSuccess("Provincia creada con éxito");
            } else {
                callback.onError("Error al crear Provincia");
            }
        }).start();
    }

    public void editarProvincia(Provincia provincia, ProvinciaCallback callback) {
        // Realizar la operación de edición de Provincia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = provinciaDAO.editarProvincia(provincia);
            if (resultado) {
                callback.onSuccess("Provincia editada con éxito");
            } else {
                callback.onError("Error al editar Provincia");
            }
        }).start();
    }

    public void borrarProvincia(int idProvincia, ProvinciaCallback callback) {
        // Realizar la operación de borrado de Provincia por IdProvincia en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = provinciaDAO.borrarProvincia(idProvincia);
            if (resultado) {
                callback.onSuccess("Provincia borrada con éxito");
            } else {
                callback.onError("Error al borrar Provincia");
            }
        }).start();
    }

    public void traerProvinciaPorId(int idProvincia, ProvinciaCallback callback) {
        // Realizar la operación de traer Provincia por IdProvincia en un hilo o AsyncTask
        new Thread(() -> {
            Provincia provincia = provinciaDAO.traerProvinciaPorId(idProvincia);
            if (provincia != null) {
                callback.onProvinciaLoaded(provincia);
            } else {
                callback.onError("Provincia no encontrada");
            }
        }).start();
    }

    public void traerTodasLasProvincias(ProvinciasCallback callback) {
        // Realizar la operación de traer todas las Provincias en un hilo o AsyncTask
        new Thread(() -> {
            List<Provincia> provincias = provinciaDAO.traerTodasLasProvincias();
            callback.onProvinciasLoaded(provincias);
        }).start();
    }

    public interface ProvinciaCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onProvinciaLoaded(Provincia provincia);
    }

    public interface ProvinciasCallback {
        void onProvinciasLoaded(List<Provincia> provincias);
    }
}
