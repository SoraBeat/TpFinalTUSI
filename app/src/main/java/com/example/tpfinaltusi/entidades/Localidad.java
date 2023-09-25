package com.example.tpfinaltusi.entidades;

public class Localidad {
    private int idProvincia;
    private int idLocalidad;
    private String nombre;

    // Constructor
    public Localidad(int idProvincia, int idLocalidad, String nombre) {
        this.idProvincia = idProvincia;
        this.idLocalidad = idLocalidad;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Localidad{" +
                "idProvincia=" + idProvincia +
                ", idLocalidad=" + idLocalidad +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
