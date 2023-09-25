package com.example.tpfinaltusi.entidades;

public class NivelRiesgo {
    private int idNivel;
    private String nombre;

    // Constructor
    public NivelRiesgo(int idNivel, String nombre) {
        this.idNivel = idNivel;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "NivelRiesgo{" +
                "idNivel=" + idNivel +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
