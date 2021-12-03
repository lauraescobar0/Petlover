package com.example.petlover.pojo;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.List;

public class PublicacionOferta {

    @Exclude
    private String idPublicacion;
    private String categoria; // Producto o Servicio
    private String Titulo;
    private String descripcion;
    private String fotoPrincipal;
    private Date fechaPublicacion;
    private String idUsuarioPublicante;
    private List<String> listaFavoritosPublicacionIdUsuarios;

    public String getIdUsuarioPublicante() {
        return idUsuarioPublicante;
    }

    public void setIdUsuarioPublicante(String idUsuarioPublicante) {
        this.idUsuarioPublicante = idUsuarioPublicante;
    }

    public PublicacionOferta(){}

    @Exclude
    public String getIdPublicacion() {
        return idPublicacion;
    }

    @Exclude
    public void setIdPublicacion(String idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public List<String> getListaFavoritosPublicacionIdUsuarios() {
        return listaFavoritosPublicacionIdUsuarios;
    }

    public void setListaFavoritosPublicacionIdUsuarios(List<String> listaFavoritosPublicacionIdUsuarios) {
        this.listaFavoritosPublicacionIdUsuarios = listaFavoritosPublicacionIdUsuarios;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFotoPrincipal() {
        return fotoPrincipal;
    }

    public void setFotoPrincipal(String fotoPrincipal) {
        this.fotoPrincipal = fotoPrincipal;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

}
