package com.example.petlover.pojo;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.List;

public class ServicioClinica {

    @Exclude
    private String idServicio;
    private String nombreServicio;
    private boolean disponible;
    private String Descripcion;
    private String fotoPerfilServicio;
    private List<String> urlFotosServicios;
    private String precio;
    private Date fechaInsercion;

    public ServicioClinica(){}

    @Exclude
    public String getIdServicio() {
        return idServicio;
    }

    @Exclude
    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFotoPerfilServicio() {
        return fotoPerfilServicio;
    }

    public void setFotoPerfilServicio(String fotoPerfilServicio) {
        this.fotoPerfilServicio = fotoPerfilServicio;
    }

    public List<String> getUrlFotosServicios() {
        return urlFotosServicios;
    }

    public void setUrlFotosServicios(List<String> urlFotosServicios) {
        this.urlFotosServicios = urlFotosServicios;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Date getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(Date fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

}
