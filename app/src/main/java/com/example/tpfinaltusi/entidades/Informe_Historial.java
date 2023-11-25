package com.example.tpfinaltusi.entidades;

import java.util.Date;

public class Informe_Historial {
    private int idInforme_Historial;
    private int idInforme;
    private String IMG;
    private String IMG_Prueba;
    private Date fecha;
    private int idEstado;
    private String titulo;
    private String cuerpo;
    private boolean ocultar;
    private boolean resultado;
    private int idUsuario;

    public Informe_Historial(int idInforme_Historial, int idInforme, String IMG, String IMG_Prueba, Date fecha, int idEstado, String titulo, String cuerpo, boolean ocultar, boolean resultado, int idUsuario) {
        this.idInforme_Historial = idInforme_Historial;
        this.idInforme = idInforme;
        this.IMG = IMG;
        this.IMG_Prueba = IMG_Prueba;
        this.fecha = fecha;
        this.idEstado = idEstado;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.ocultar = ocultar;
        this.resultado = resultado;
        this.idUsuario = idUsuario;
    }
    public Informe_Historial(){

    }

    public int getIdInforme_Historial() {
        return idInforme_Historial;
    }

    public void setIdInforme_Historial(int idInforme_Historial) {
        this.idInforme_Historial = idInforme_Historial;
    }

    public int getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(int idInforme) {
        this.idInforme = idInforme;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getIMG_Prueba() {
        return IMG_Prueba;
    }

    public void setIMG_Prueba(String IMG_Prueba) {
        this.IMG_Prueba = IMG_Prueba;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public boolean isOcultar() {
        return ocultar;
    }

    public void setOcultar(boolean ocultar) {
        this.ocultar = ocultar;
    }

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
