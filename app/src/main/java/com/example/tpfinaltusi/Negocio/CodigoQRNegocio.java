package com.example.tpfinaltusi.Negocio;

import com.example.tpfinaltusi.DAO.CodigoQRDAO;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.util.List;

public class CodigoQRNegocio {
    private CodigoQRDAO codigoQRDAO;

    public CodigoQRNegocio() {
        codigoQRDAO = new CodigoQRDAO();
    }

    public void crearCodigoQR(CodigoQR codigoQR, CodigoQRCallback callback) {
        new Thread(() -> {
            boolean resultado = codigoQRDAO.crearCodigoQR(codigoQR);
            if (resultado) {
                callback.onSuccess("Código QR creado con éxito");
            } else {
                callback.onError("Error al crear Código QR");
            }
        }).start();
    }

    public void editarCodigoQR(CodigoQR codigoQR, CodigoQRCallback callback) {
        new Thread(() -> {
            boolean resultado = codigoQRDAO.editarCodigoQR(codigoQR);
            if (resultado) {
                callback.onSuccess("Código QR editado con éxito");
            } else {
                callback.onError("Error al editar Código QR");
            }
        }).start();
    }

    public void borrarCodigoQR(int idCodigoQR, CodigoQRCallback callback) {
        new Thread(() -> {
            boolean resultado = codigoQRDAO.borrarCodigoQR(idCodigoQR);
            if (resultado) {
                callback.onSuccess("Código QR borrado con éxito");
            } else {
                callback.onError("Error al borrar Código QR");
            }
        }).start();
    }

    public void traerCodigoQRPorId(int idCodigoQR, CodigoQRCallback callback) {
        new Thread(() -> {
            CodigoQR codigoQR = codigoQRDAO.traerCodigoQRPorId(idCodigoQR);
            if (codigoQR != null) {
                callback.onCodigoQRLoaded(codigoQR);
            } else {
                callback.onError("Código QR no encontrado");
            }
        }).start();
    }

    public void traerTodosLosCodigoQRs(CodigoQRsCallback callback) {
        new Thread(() -> {
            List<CodigoQR> codigoQRs = codigoQRDAO.traerTodosLosCodigoQRs();
            callback.onCodigoQRsLoaded(codigoQRs);
        }).start();
    }

    public interface CodigoQRCallback {
        void onSuccess(String mensaje);

        void onError(String error);

        void onCodigoQRLoaded(CodigoQR codigoQR);
    }

    public interface CodigoQRsCallback {
        void onCodigoQRsLoaded(List<CodigoQR> codigoQRs);
    }
    public void editarEstadoCanjeado(String codigo, boolean nuevoEstado, CodigoQRCallback callback) {
        new Thread(() -> {
            boolean resultado = codigoQRDAO.editarEstadoCanjeado(codigo, nuevoEstado);
            if (resultado) {
                callback.onSuccess("Estado canjeado cambiado con éxito");
            } else {
                callback.onError("Error al cambiar el estado canjeado");
            }
        }).start();
    }

}
