package com.example.tpfinaltusi.Negocio;

import android.os.Handler;
import android.os.Looper;

import com.example.tpfinaltusi.DAO.InformeDAO;
import com.example.tpfinaltusi.entidades.Informe;

import java.util.List;

public class InformeNegocio {

    private InformeDAO informeDAO;

    public InformeNegocio() {
        informeDAO = new InformeDAO();
    }

    public void crearInforme(Informe informe) {
        new Thread(() -> {
            boolean resultado = informeDAO.crearInforme(informe);
        }).start();
    }

    public void editarInforme(Informe informe, InformeCallback callback) {
        // Realizar la operación de edición de Informe en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeDAO.editarInforme(informe);
            if (resultado) {
                callback.onSuccess("Informe editado con éxito");
            } else {
                callback.onError("Error al editar Informe");
            }
        }).start();
    }

    public void borrarInforme(int idInforme, InformeCallback callback) {
        // Realizar la operación de borrado de Informe por IdInforme en un hilo o AsyncTask
        new Thread(() -> {
            boolean resultado = informeDAO.borrarInforme(idInforme);
            if (resultado) {
                callback.onSuccess("Informe borrado con éxito");
            } else {
                callback.onError("Error al borrar Informe");
            }
        }).start();
    }

    public void traerInformePorId(int idInforme, InformeCallback callback) {
        // Realizar la operación de traer Informe por IdInforme en un hilo o AsyncTask
        new Thread(() -> {
            Informe informe = informeDAO.traerInformePorId(idInforme);
            if (informe != null) {
                callback.onInformeLoaded(informe);
            } else {
                callback.onError("Informe no encontrado");
            }
        }).start();
    }

    public void traerTodosLosInformes(InformesCallback callback) {
        // Realizar la operación de traer todos los Informes en un hilo o AsyncTask
        new Thread(() -> {
            List<Informe> informes = informeDAO.traerTodosLosInformes();
            if(informes!= null){
                runOnUiThread(() -> {
                    callback.onInformesLoaded(informes);
                });
            } else {
                runOnUiThread(() -> {
                    callback.onError("Error al obtener los reportes");
                });
            }
        }).start();
    }
    private void runOnUiThread(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
    public interface InformeCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onInformeLoaded(Informe informe);
    }

    public interface InformesCallback {
        void onInformesLoaded(List<Informe> informes);
        void onError(String error);
    }
}
