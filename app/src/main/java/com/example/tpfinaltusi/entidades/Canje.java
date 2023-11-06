package com.example.tpfinaltusi.entidades;

public class Canje {
    private int idCanje;
    private int idUsuario;
    private int idPremio;
    private int idPuntoVerde;
    private int cantidad;
    private int precio;
    private boolean estado;

    // Constructor
    public Canje(int idCanje, int idUsuario, int idPremio, int idPuntoVerde, int cantidad, int precio,boolean estado) {
        this.idCanje = idCanje;
        this.idUsuario = idUsuario;
        this.idPremio = idPremio;
        this.idPuntoVerde = idPuntoVerde;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Canje( int idUsuario, int idPremio, int idPuntoVerde, int cantidad, int precio, boolean estado) {;
        this.idUsuario = idUsuario;
        this.idPremio = idPremio;
        this.idPuntoVerde = idPuntoVerde;
        this.cantidad = cantidad;
        this.precio = precio;
        this.estado = estado;
    }
    // Getters y Setters

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getIdCanje() {
        return idCanje;
    }

    public void setIdCanje(int idCanje) {
        this.idCanje = idCanje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPremio() {
        return idPremio;
    }

    public void setIdPremio(int idPremio) {
        this.idPremio = idPremio;
    }

    public int getIdPuntoVerde() {
        return idPuntoVerde;
    }

    public void setIdPuntoVerde(int idPuntoVerde) {
        this.idPuntoVerde = idPuntoVerde;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Canje{" +
                "idCanje=" + idCanje +
                ", idUsuario=" + idUsuario +
                ", idPremio=" + idPremio +
                ", idPuntoVerde=" + idPuntoVerde +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
