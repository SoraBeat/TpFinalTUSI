package com.example.tpfinaltusi.entidades;

import java.util.Date;

public class Informe {
    private int idInforme;
    private String titulo;
    private String cuerpo;
    private int idNivel;
    private Date fechaAlta;
    private int usuarioAlta;
    private Date fechaBaja;
    private int usuarioBaja;
    private int puntosRecompensa;
    private int idEstado;
    private double latitud;
    private double longitud;
    private String imagen;

    // Constructor

    public Informe(int idInforme, String titulo, String cuerpo, int idNivel, Date fechaAlta, int usuarioAlta, Date fechaBaja, int usuarioBaja, int puntosRecompensa, int idEstado, double latitud, double longitud, String imagen) {
        this.idInforme = idInforme;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.idNivel = idNivel;
        this.fechaAlta = fechaAlta;
        this.usuarioAlta = usuarioAlta;
        this.fechaBaja = fechaBaja;
        this.usuarioBaja = usuarioBaja;
        this.puntosRecompensa = puntosRecompensa;
        this.idEstado = idEstado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
    }

    public Informe(String titulo, String cuerpo, int usuarioAlta, int idEstado, double latitud, double longitud, String imagen) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.usuarioAlta = usuarioAlta;
        this.idEstado = idEstado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
    }

    public Informe() {

    }

    // Getters y Setters
    public int getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(int idInforme) {
        this.idInforme = idInforme;
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

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public int getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(int usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public int getUsuarioBaja() {
        return usuarioBaja;
    }

    public void setUsuarioBaja(int usuarioBaja) {
        this.usuarioBaja = usuarioBaja;
    }

    public int getPuntosRecompensa() {
        return puntosRecompensa;
    }
    public  int getIdEstado(){return idEstado;}

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setPuntosRecompensa(int puntosRecompensa) {
        this.puntosRecompensa = puntosRecompensa;
    }

    @Override
    public String toString() {
        return "Informe{" +
                "idInforme=" + idInforme +
                ", titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", idNivel=" + idNivel +
                ", fechaAlta=" + fechaAlta +
                ", usuarioAlta=" + usuarioAlta +
                ", fechaBaja=" + fechaBaja +
                ", usuarioBaja=" + usuarioBaja +
                ", puntosRecompensa=" + puntosRecompensa +
                '}';
    }
}
