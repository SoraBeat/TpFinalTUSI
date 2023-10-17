package com.example.tpfinaltusi.entidades;

public class CodigoQR {
    private int idCodigoQR;
    private String codigo;
    private String imagen;
    private int puntos;

    public CodigoQR(int idCodigoQR, String codigo, String imagen, int puntos) {
        this.idCodigoQR = idCodigoQR;
        this.codigo = codigo;
        this.imagen = imagen;
        this.puntos = puntos;
    }

    public int getIdCodigoQR() {
        return idCodigoQR;
    }

    public void setIdCodigoQR(int idCodigoQR) {
        this.idCodigoQR = idCodigoQR;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "CodigoQR{" +
                "idCodigoQR=" + idCodigoQR +
                ", codigo='" + codigo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", puntos=" + puntos +
                '}';
    }
}
