package com.example.tpfinaltusi.entidades;

public class CodigoQR {
    private int idCodigoQR;
    private String codigo;
    private String imagen;
    private int puntos;
    private boolean canjeado;

    private int usuarioQueLoCreo;

    public CodigoQR(int idCodigoQR, String codigo, String imagen, int puntos, boolean canjeado, int usuarioQueLoCreo) {
        this.idCodigoQR = idCodigoQR;
        this.codigo = codigo;
        this.imagen = imagen;
        this.puntos = puntos;
        this.canjeado = canjeado;
        this.usuarioQueLoCreo = usuarioQueLoCreo;
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
    public int getUsuarioQueLoCreo() {
        return usuarioQueLoCreo;
    }

    public void setUsuarioQueLoCreo(int usuarioQueLoCreo) {
        this.usuarioQueLoCreo = usuarioQueLoCreo;
    }
    public boolean isCanjeado() {
        return canjeado;
    }

    public void setCanjeado(boolean canjeado) {
        this.canjeado = canjeado;
    }

    @Override
    public String toString() {
        return "CodigoQR{" +
                "idCodigoQR=" + idCodigoQR +
                ", codigo='" + codigo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", puntos=" + puntos +
                ", canjeado=" + canjeado +
                ", usuarioQueLoCreo=" + usuarioQueLoCreo +
                '}';
    }
}
