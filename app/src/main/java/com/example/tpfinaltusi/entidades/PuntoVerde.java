package com.example.tpfinaltusi.entidades;

public class PuntoVerde {
    private int idPuntoVerde;
    private String titulo;
    private int idLocalidad;
    private String calleAltura;

    // Constructor
    public PuntoVerde(int idPuntoVerde, String titulo, int idLocalidad, String calleAltura) {
        this.idPuntoVerde = idPuntoVerde;
        this.titulo = titulo;
        this.idLocalidad = idLocalidad;
        this.calleAltura = calleAltura;
    }

    // Getters y Setters
    public int getIdPuntoVerde() {
        return idPuntoVerde;
    }

    public void setIdPuntoVerde(int idPuntoVerde) {
        this.idPuntoVerde = idPuntoVerde;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getCalleAltura() {
        return calleAltura;
    }

    public void setCalleAltura(String calleAltura) {
        this.calleAltura = calleAltura;
    }

    @Override
    public String toString() {
        return "PuntoVerde{" +
                "idPuntoVerde=" + idPuntoVerde +
                ", titulo='" + titulo + '\'' +
                ", idLocalidad=" + idLocalidad +
                ", calleAltura='" + calleAltura + '\'' +
                '}';
    }
}
