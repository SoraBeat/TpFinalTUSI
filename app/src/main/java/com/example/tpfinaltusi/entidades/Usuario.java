package com.example.tpfinaltusi.entidades;

import java.util.Date;

public class Usuario {
    private String alias;
    private String dni;
    private String email;
    private String password;
    private int cantPuntos;
    private Date fechaAlta;
    private Date fechaBaja;

    // Constructor
    public Usuario(String alias, String dni, String email, String password, int cantPuntos, Date fechaAlta, Date fechaBaja) {
        this.alias = alias;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.cantPuntos = cantPuntos;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    // Getters y Setters
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCantPuntos() {
        return cantPuntos;
    }

    public void setCantPuntos(int cantPuntos) {
        this.cantPuntos = cantPuntos;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "alias='" + alias + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cantPuntos=" + cantPuntos +
                ", fechaAlta=" + fechaAlta +
                ", fechaBaja=" + fechaBaja +
                '}';
    }
}
