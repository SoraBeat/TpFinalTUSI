package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.PuntoVerde_PremioDAO;
import com.example.tpfinaltusi.entidades.PuntoVerde_Premio;

import java.util.List;

public class PuntoVerde_PremioNegocio {

    private PuntoVerde_PremioDAO puntoVerde_PremioDAO;

    public PuntoVerde_PremioNegocio() {
        puntoVerde_PremioDAO = new PuntoVerde_PremioDAO();
    }

    public void crearPuntoVerde_Premio(PuntoVerde_Premio puntoVerde_Premio, PuntoVerde_PremioCallback callback) {
        // Realizar la operación de creación de PuntoVerde_Premio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerde_PremioDAO.crearPuntoVerde_Premio(puntoVerde_Premio);
            if (resultado) {
                callback.onSuccess("Relación PuntoVerde_Premio creada con éxito");
            } else {
                callback.onError("Error al crear Relación PuntoVerde_Premio");
            }
        }).start();
    }

    public void editarPuntoVerde_Premio(PuntoVerde_Premio puntoVerde_Premio, PuntoVerde_PremioCallback callback) {
        // Realizar la operación de edición de PuntoVerde_Premio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerde_PremioDAO.editarPuntoVerde_Premio(puntoVerde_Premio);
            if (resultado) {
                callback.onSuccess("Relación PuntoVerde_Premio editada con éxito");
            } else {
                callback.onError("Error al editar Relación PuntoVerde_Premio");
            }
        }).start();
    }

    public void borrarPuntoVerde_Premio(int idPuntoVerdePremio, PuntoVerde_PremioCallback callback) {
        // Realizar la operación de borrado de PuntoVerde_Premio por IdPuntoVerdePremio en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerde_PremioDAO.borrarPuntoVerde_Premio(idPuntoVerdePremio);
            if (resultado) {
                callback.onSuccess("Relación PuntoVerde_Premio borrada con éxito");
            } else {
                callback.onError("Error al borrar Relación PuntoVerde_Premio");
            }
        }).start();
    }

    public void traerPuntoVerde_PremioPorId(int idPuntoVerde, int idPremio, PuntoVerde_PremioCallback callback) {
        // Realizar la operación de traer PuntoVerde_Premio por IdPuntoVerdePremio en un hilo o AsyncTask
        new Thread(() -> {
            PuntoVerde_Premio puntoVerde_Premio = puntoVerde_PremioDAO.traerPuntoVerde_PremioPorId(idPuntoVerde,idPremio);
            if (puntoVerde_Premio != null) {
                callback.onPuntoVerde_PremioLoaded(puntoVerde_Premio);
            } else {
                callback.onError("Relación PuntoVerde_Premio no encontrada");
            }
        }).start();
    }

    public void traerTodasLasPuntosVerdes_Premios(PuntoVerde_PremiosCallback callback) {
        // Realizar la operación de traer todas las relaciones PuntoVerde_Premio en un hilo o AsyncTask
        new Thread(() -> {
            List<PuntoVerde_Premio> puntosVerdes_Premios = puntoVerde_PremioDAO.traerTodasLasPuntosVerdes_Premios();
            callback.onPuntoVerde_PremiosLoaded(puntosVerdes_Premios);
        }).start();
    }

    public void restarStockPuntoVerde(int Arestar,int idPuntoVerde, int idPremio,PuntoVerde_PremioCallback callback){
        // Realizar la operación de restar stock en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = puntoVerde_PremioDAO.restarStock(Arestar,idPuntoVerde,idPremio);
            if (resultado) {
                callback.onSuccess("Puntos restados con éxito");
            } else {
                callback.onError("Error al restados puntos");
            }
        }).start();
    }

    public interface PuntoVerde_PremioCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onPuntoVerde_PremioLoaded(PuntoVerde_Premio puntoVerde_Premio);
    }

    public interface PuntoVerde_PremiosCallback {
        void onPuntoVerde_PremiosLoaded(List<PuntoVerde_Premio> puntosVerdes_Premios);
    }
}
