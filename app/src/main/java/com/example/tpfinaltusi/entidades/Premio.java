package com.example.tpfinaltusi.entidades;

public class Premio {
    private int idPremio;
    private String nombre;
    private String imagen;
    private int precio;

    // Constructor
    public Premio(int idPremio, String nombre, String imagen, int precio) {
        this.idPremio = idPremio;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precio = precio;
    }
    public Premio() {

    }

    // Getters y Setters
    public int getIdPremio() {
        return idPremio;
    }

    public void setIdPremio(int idPremio) {
        this.idPremio = idPremio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Premio{" +
                "idPremio=" + idPremio +
                ", nombre='" + nombre + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precio=" + precio +
                '}';
    }
}
