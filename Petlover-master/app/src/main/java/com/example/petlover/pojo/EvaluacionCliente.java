package com.example.petlover.pojo;

import com.google.firebase.firestore.Exclude;

public class EvaluacionCliente {
    @Exclude
    private String idEvaluacion;
    private String nombreClinica;
    private String usuario;
    private String estrellas; // 1 a 5
    private String comentario;
    private String idClinica;

    public EvaluacionCliente(){}

    @Exclude
    public String getIdEvaluacion() {
        return idEvaluacion;
    }

    @Exclude
    public void setIdEvaluacion(String idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getNombreClinica() {
        return nombreClinica;
    }

    public void setNombreClinica(String nombreClinica) {
        this.nombreClinica = nombreClinica;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(String estrellas) {
        this.estrellas = estrellas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(String idClinica) {
        this.idClinica = idClinica;
    }
}
