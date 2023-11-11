package com.example.tpfinaltusi.entidades;

public class Informe_Imagen {
    private int idInformeImagen;
    private int idInforme;
    private String imagen;

    // Constructor
    public Informe_Imagen(int idInformeImagen, int idInforme, String imagen) {
        this.idInformeImagen = idInformeImagen;
        this.idInforme = idInforme;
        this.imagen = imagen;
    }
    public Informe_Imagen(int idInforme, String imagen) {
        this.idInforme = idInforme;
        this.imagen = imagen;
    }

    // Getters y Setters
    public int getIdInformeImagen() {
        return idInformeImagen;
    }

    public void setIdInformeImagen(int idInformeImagen) {
        this.idInformeImagen = idInformeImagen;
    }

    public int getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(int idInforme) {
        this.idInforme = idInforme;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Informe_Imagen{" +
                "idInformeImagen=" + idInformeImagen +
                ", idInforme=" + idInforme +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}
