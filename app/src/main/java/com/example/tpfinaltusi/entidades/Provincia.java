package com.example.tpfinaltusi.entidades;

public class Provincia {
    private int idProvincia;
    private int idPais; // Clave foránea que hace referencia al ID del país
    private String nombre;

    // Constructor
    public Provincia(int idProvincia, int idPais, String nombre) {
        this.idProvincia = idProvincia;
        this.idPais = idPais;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

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
        return "Provincia{" +
                "idProvincia=" + idProvincia +
                ", idPais=" + idPais +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
