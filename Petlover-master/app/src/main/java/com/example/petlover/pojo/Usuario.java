package com.example.petlover.pojo;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Usuario {

    public static final String ID="id";
    public static final String NOMBRES="nombres";
    public static final String APELLIDOS="apellidos";
    public static final String CORREO="correo";
    public static final String USUARIO="usuario";
    public static final String ROLUSUARIO_CAMPO="rolUsuario";
    public static final String FOTOURL="fotoUrl";
    public static final String PROVEEDOR="proveedor";
    public static final String DUI_="DUI";
    public static final String TELEFONO="telefonoContacto";
    public static final String REDES="redesSociales";
    public static final String PERFILES="nombrePerfiles";
    public static final String ROL_USUARIO="Usuario";
    public static final String ROL_PROPIETARIO="Propietario";
    public static final String LISTA_ID_CLINICAS="listaIdsClinicas";

    @Exclude
    // Para que la carga y descarga de este parametro no sea considerada por FIREBASE, es decir lo excluya.
    private String id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String usuario;
    private String fotoUrl;
    private String proveedor; // Correo, Google, o Facebook
    private String rolUsuario; //Por defecto Cliente, y podrá ser dado de alta como proveedor de servicio
    private String DUI;
    private String telefonoContacto;
    private List<String> redesSociales;
    private List<String> nombrePerfiles;
    private List<String> listaIdsClinicas;

    public Usuario(){}

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public List<String> getListaIdsClinicas() {
        return listaIdsClinicas;
    }

    public void setListaIdsClinicas(List<String> listaIdsClinicas) {
        this.listaIdsClinicas = listaIdsClinicas;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getDUI() {
        return DUI;
    }

    public void setDUI(String DUI) {
        this.DUI = DUI;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public List<String> getRedesSociales() {
        return redesSociales;
    }

    public void setRedesSociales(List<String> redesSociales) {
        this.redesSociales = redesSociales;
    }

    public List<String> getNombrePerfiles() {
        return nombrePerfiles;
    }

    public void setNombrePerfiles(List<String> nombrePerfiles) {
        this.nombrePerfiles = nombrePerfiles;
    }

}
