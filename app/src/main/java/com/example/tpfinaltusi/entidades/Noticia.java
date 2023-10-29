package com.example.tpfinaltusi.entidades;

import java.util.Date;

public class Noticia {
    private int idNoticia;
    private String titulo;
    private String cuerpo;
    private String categoria;
    private String imagen;
    private Date fechaAlta;
    private int idLocalidad;

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public String getTagMaps() {
        return tagMaps;
    }

    public void setTagMaps(String tagMaps) {
        this.tagMaps = tagMaps;
    }

    private float latitud;
    private float longitud;
    private String tagMaps;

    // Constructor
    public Noticia(int idNoticia, String titulo, String cuerpo, String categoria, String imagen, Date fechaAlta, int idLocalidad, float latitud, float longitud, String tagMaps) {
        this.idNoticia = idNoticia;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.categoria = categoria;
        this.imagen = imagen;
        this.fechaAlta = fechaAlta;
        this.idLocalidad = idLocalidad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tagMaps = tagMaps;
    }

    public Noticia(String titulo, String cuerpo, String categoria, String imagen, Date fechaAlta, int idLocalidad, float latitud, float longitud, String tagMaps) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.categoria = categoria;
        this.imagen = imagen;
        this.fechaAlta = fechaAlta;
        this.idLocalidad = idLocalidad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tagMaps = tagMaps;
    }

    // Getters y Setters
    public int getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(int idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    @Override
    public String toString() {
        return "Noticia{" +
                "idNoticia=" + idNoticia +
                ", titulo='" + titulo + '\'' +
                ", cuerpo='" + cuerpo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", imagen='" + imagen + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", idLocalidad=" + idLocalidad +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", tagMaps='" + tagMaps + '\'' +
                '}';
    }
}
