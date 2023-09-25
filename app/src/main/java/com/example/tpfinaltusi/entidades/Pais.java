package com.example.tpfinaltusi.entidades;

public class Pais {
    private int idPais;
    private String nombre;

    // Constructor
    public Pais(int idPais, String nombre) {
        this.idPais = idPais;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Pais{" +
                "idPais=" + idPais +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
