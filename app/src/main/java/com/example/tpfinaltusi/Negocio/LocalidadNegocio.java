package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.LocalidadDAO;
import com.example.tpfinaltusi.entidades.Localidad;

import java.util.List;

public class LocalidadNegocio {

    private LocalidadDAO localidadDAO;

    public LocalidadNegocio() {
        localidadDAO = new LocalidadDAO();
    }

    public void crearLocalidad(Localidad localidad, LocalidadCallback callback) {
        // Realizar la operación de creación de Localidad en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = localidadDAO.crearLocalidad(localidad);
            if (resultado) {
                callback.onSuccess("Localidad creada con éxito");
            } else {
                callback.onError("Error al crear Localidad");
            }
        }).start();
    }

    public void editarLocalidad(Localidad localidad, LocalidadCallback callback) {
        // Realizar la operación de edición de Localidad en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = localidadDAO.editarLocalidad(localidad);
            if (resultado) {
                callback.onSuccess("Localidad editada con éxito");
            } else {
                callback.onError("Error al editar Localidad");
            }
        }).start();
    }

    public void borrarLocalidad(int idProvincia, int idLocalidad, LocalidadCallback callback) {
        // Realizar la operación de borrado de Localidad por IdProvincia e IdLocalidad en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = localidadDAO.borrarLocalidad(idProvincia, idLocalidad);
            if (resultado) {
                callback.onSuccess("Localidad borrada con éxito");
            } else {
                callback.onError("Error al borrar Localidad");
            }
        }).start();
    }

    public void traerLocalidadPorIds(int idProvincia, int idLocalidad, LocalidadCallback callback) {
        // Realizar la operación de traer Localidad por IdProvincia e IdLocalidad en un hilo o AsyncTask
        new Thread(() -> {
            Localidad localidad = localidadDAO.traerLocalidadPorIds(idProvincia, idLocalidad);
            if (localidad != null) {
                callback.onLocalidadLoaded(localidad);
            } else {
                callback.onError("Localidad no encontrada");
            }
        }).start();
    }

    public void traerLocalidadPorId(int idLocalidad, LocalidadCallback callback) {
        // Realizar la operación de traer Localidad por IdProvincia e IdLocalidad en un hilo o AsyncTask
        new Thread(() -> {
            Localidad localidad = localidadDAO.traerLocalidadPorId(idLocalidad);
            if (localidad != null) {
                callback.onLocalidadLoaded(localidad);
            } else {
                callback.onError("Localidad no encontrada");
            }
        }).start();
    }

    public void traerLocalidadesPorProvincia(int idLocalidad, LocalidadesCallback callback) {
        // Realizar la operación de traer todas las Localidades de una Provincia en un hilo o AsyncTask
        new Thread(() -> {
            List<Localidad> localidades = localidadDAO.traerLocalidadesPorProvincia(idLocalidad);
            callback.onLocalidadesLoaded(localidades);
        }).start();
    }

    public interface LocalidadCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onLocalidadLoaded(Localidad localidad);
    }

    public interface LocalidadesCallback {
        void onLocalidadesLoaded(List<Localidad> localidades);
    }
}
