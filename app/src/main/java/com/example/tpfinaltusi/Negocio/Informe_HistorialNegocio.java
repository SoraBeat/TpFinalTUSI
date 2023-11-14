package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.Informe_HistorialDAO;
import com.example.tpfinaltusi.DAO.Informe_ImagenDAO;
import com.example.tpfinaltusi.entidades.Informe_Historial;
import com.example.tpfinaltusi.entidades.Informe_Imagen;

import java.util.List;

public class Informe_HistorialNegocio {
    private Informe_HistorialDAO informeHistorialDAO;

    public Informe_HistorialNegocio() {
        informeHistorialDAO = new Informe_HistorialDAO();
    }

    public void crearInforme_Historial(Informe_Historial informeHistorial, Informe_HistorialCallback callback) {
        // Realizar la operación de creación de Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeHistorialDAO.crearInforme_Historial(informeHistorial);
            if (resultado) {
                callback.onSuccess("Informe_Historial creado con éxito");
            } else {
                callback.onError("Error al crear Informe_Historial");
            }
        }).start();
    }
    public void ocultarInforme_Historial(int idInformeHistorial, Informe_HistorialCallback callback) {
        // Realizar la operación de edición de Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeHistorialDAO.ocultarInforme_Imagen(idInformeHistorial);
            if (resultado) {
                callback.onSuccess("Informe_Historial ocultado con éxito");
            } else {
                callback.onError("Error al ocultar Informe_Historial");
            }
        }).start();
    }
    public void traerInformeHistorialPorId(int idInformeImagen, Informe_HistorialCallback callback) {
        // Realizar la operación de traer Informe_Imagen por IdInformeImagen en un hilo o AsyncTask
        new Thread(() -> {
            Informe_Historial informe_historial = informeHistorialDAO.traerInforme_HistorialPorId(idInformeImagen);
            if (informe_historial != null) {
                callback.onInformeHistorialLoaded(informe_historial);
            } else {
                callback.onError("Informe_Historial no encontrado");
            }
        }).start();
    }

    public void traerTodosLosInformesHistoriales(Informe_HistorialesCallback callback) {
        // Realizar la operación de traer todas las Informe_Imagen en un hilo o AsyncTask
        new Thread(() -> {
            List<Informe_Historial> informeHistoriales = informeHistorialDAO.traerTodosLosInformes_Historial();
            callback.onInformeHistorialesLoaded(informeHistoriales);
        }).start();
    }

    public interface Informe_HistorialCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onInformeHistorialLoaded(Informe_Historial informeHistorial);
    }
    public interface Informe_HistorialesCallback {
        void onInformeHistorialesLoaded(List<Informe_Historial> informeHistorial);
    }
}