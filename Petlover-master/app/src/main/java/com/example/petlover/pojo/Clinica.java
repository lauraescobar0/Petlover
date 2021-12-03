package com.example.petlover.pojo;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Clinica {

    @Exclude
    private String idClinica;
    private String nombreClinica;
    private String direccion;
    private String numeroTelefo;
    private String numeroCelular;
    private List<String> redesSociales;
    private List<String> perfilesRedesSociales;
    private String correoElectronico;
    private String sitioWeb;
    private String Encargado; //Doctor jefe o propietario
    private Date fechaApertura;
    private String fotoPerfilUrl;
    private String idUsuarioRegistro;
    private String idPublicacionFijada;
    private String descripcion;

    public Clinica(){}

    public String getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(String idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public String getIdPublicacionFijada() {
        return idPublicacionFijada;
    }

    public void setIdPublicacionFijada(String idPublicacionFijada) {
        this.idPublicacionFijada = idPublicacionFijada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Exclude
    public String getIdClinica() {
        return idClinica;
    }

    @Exclude
    public void setIdClinica(String idClinica) {
        this.idClinica = idClinica;
    }

    public String getNombreClinica() {
        return nombreClinica;
    }

    public void setNombreClinica(String nombreClinica) {
        this.nombreClinica = nombreClinica;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroTelefo() {
        return numeroTelefo;
    }

    public void setNumeroTelefo(String numeroTelefo) {
        this.numeroTelefo = numeroTelefo;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public List<String> getRedesSociales() {
        return redesSociales;
    }

    public void setRedesSociales(List<String> redesSociales) {
        this.redesSociales = redesSociales;
    }

    public List<String> getPerfilesRedesSociales() {
        return perfilesRedesSociales;
    }

    public void setPerfilesRedesSociales(List<String> perfilesRedesSociales) {
        this.perfilesRedesSociales = perfilesRedesSociales;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getEncargado() {
        return Encargado;
    }

    public void setEncargado(String encargado) {
        Encargado = encargado;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clinica clinica = (Clinica) o;
        return Objects.equals(idClinica, clinica.getIdClinica());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClinica);
    }


}
