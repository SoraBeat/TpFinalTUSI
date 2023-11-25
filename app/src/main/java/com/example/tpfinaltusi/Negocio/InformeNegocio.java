package com.example.tpfinaltusi.Negocio;

import android.os.Handler;
import android.os.Looper;

import com.example.tpfinaltusi.DAO.InformeDAO;
import com.example.tpfinaltusi.DAO.Informe_HistorialDAO;
import com.example.tpfinaltusi.DAO.Informe_ImagenDAO;
import com.example.tpfinaltusi.DAO.UsuarioDAO;
import com.example.tpfinaltusi.entidades.Informe;
import com.example.tpfinaltusi.entidades.Informe_Historial;
import com.example.tpfinaltusi.entidades.Informe_Imagen;

import java.util.List;

public class InformeNegocio {

    private InformeDAO informeDAO;

    public InformeNegocio() {
        informeDAO = new InformeDAO();
    }

    public void crearInforme(Informe informe, InformeCallback callback) {
        new Thread(() -> {
            boolean resultado = informeDAO.crearInforme(informe);
            if(resultado){
                List<Informe> list = informeDAO.traerTodosLosInformes();
                Informe in = list.stream()
                        .filter(inf -> inf.getCuerpo().equals(informe.getCuerpo())
                        && inf.getTitulo().equals(informe.getTitulo())
                        && inf.getIdEstado() == 2).findFirst().orElse(new Informe());;
                Informe_Historial informe_historial = new Informe_Historial();
                informe_historial.setIdInforme(in.getIdInforme());
                informe_historial.setIMG(in.getImagen());
                informe_historial.setIdEstado(2);
                informe_historial.setTitulo(in.getTitulo());
                informe_historial.setCuerpo(in.getCuerpo());
                informe_historial.setOcultar(false);
                informe_historial.setResultado(true);
                informe_historial.setIdUsuario(in.getUsuarioAlta());
                new Informe_HistorialDAO().crearInforme_Historial(informe_historial);
                runOnUiThread(()->{
                    callback.onSuccess("Informe editado con éxito");
                });
            } else {
                runOnUiThread(()-> {
                    callback.onError("Error al crear el informe");
                });
            }
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

    public void revisionInforme(int IdInforme, int IdUsuario, String imgPrueba){
        //Pasa el informe a estado 3 (Revision)
        new Thread(()->{
            Informe informe = informeDAO.traerInformePorId(IdInforme);
            if (informe != null) {
                informe.setIdEstado(3);
                informe.setUsuarioBaja(IdUsuario);
                informeDAO.editarInforme(informe);
                Informe_Imagen img = new Informe_Imagen(IdInforme,imgPrueba);

                Informe inf = informeDAO.traerInformePorId(IdInforme);

                new Informe_ImagenDAO().crearInforme_Imagen(img);
                Informe_Historial informe_historial = new Informe_Historial();
                informe_historial.setIdInforme(IdInforme);
                informe_historial.setIMG(inf.getImagen());
                informe_historial.setIMG_Prueba(imgPrueba);
                informe_historial.setIdEstado(3);
                informe_historial.setTitulo(inf.getTitulo());
                informe_historial.setCuerpo(inf.getCuerpo());
                informe_historial.setOcultar(false);
                informe_historial.setResultado(true);
                informe_historial.setIdUsuario(IdUsuario);
                new Informe_HistorialDAO().crearInforme_Historial(informe_historial);
            }

        }).start();
    }
    public void AprobarInforme(Informe informe, InformeCallback callback){
        //Pasa el informe a estado 1 (Activo)
        new Thread(()->{
            Informe informeAUX = informeDAO.traerInformePorId(informe.getIdInforme());

            if (informeAUX != null) {
                informeAUX.setPuntosRecompensa(informe.getPuntosRecompensa());
                informeAUX.setIdEstado(1);
                if(informeDAO.editarInforme(informeAUX)){
                    callback.onSuccess("Informe aprobado");
                } else {
                    callback.onError("Hubo un error inesperado");
                }
            }

        }).start();
    }
    public void CerrarInforme(Informe informe,InformeCallback callback){
        //Pasa a estado 4 (Cerrado) y da los puntos
        new Thread(()->{
            Informe informeAUX = informeDAO.traerInformePorId(informe.getIdInforme());
            if (informeAUX !=null){
                informeAUX.setPuntosRecompensa(informe.getPuntosRecompensa());
                informeAUX.setIdEstado(4);
                if(informeDAO.editarInforme(informeAUX)){
                    if(new UsuarioDAO().sumarPuntosAUsuario(informeAUX.getUsuarioBaja(),informeAUX.getPuntosRecompensa())){
                        callback.onSuccess("");
                    } else{
                        callback.onError("");
                    }
                } else {
                    callback.onError("");
                }
            } else {
                callback.onError("");
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
