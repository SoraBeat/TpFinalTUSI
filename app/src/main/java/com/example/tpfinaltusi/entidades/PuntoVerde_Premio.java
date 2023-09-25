package com.example.tpfinaltusi.entidades;

public class PuntoVerde_Premio {
    private int idPuntoVerdePremio;
    private int idPuntoVerde;
    private int idPremio;
    private int stock;

    // Constructor
    public PuntoVerde_Premio(int idPuntoVerdePremio, int idPuntoVerde, int idPremio, int stock) {
        this.idPuntoVerdePremio = idPuntoVerdePremio;
        this.idPuntoVerde = idPuntoVerde;
        this.idPremio = idPremio;
        this.stock = stock;
    }

    // Getters y Setters
    public int getIdPuntoVerdePremio() {
        return idPuntoVerdePremio;
    }

    public void setIdPuntoVerdePremio(int idPuntoVerdePremio) {
        this.idPuntoVerdePremio = idPuntoVerdePremio;
    }

    public int getIdPuntoVerde() {
        return idPuntoVerde;
    }

    public void setIdPuntoVerde(int idPuntoVerde) {
        this.idPuntoVerde = idPuntoVerde;
    }

    public int getIdPremio() {
        return idPremio;
    }

    public void setIdPremio(int idPremio) {
        this.idPremio = idPremio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "PuntoVerde_Premio{" +
                "idPuntoVerdePremio=" + idPuntoVerdePremio +
                ", idPuntoVerde=" + idPuntoVerde +
                ", idPremio=" + idPremio +
                ", stock=" + stock +
                '}';
    }
}
