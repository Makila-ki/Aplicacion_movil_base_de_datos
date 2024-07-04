package com.example.proyecto.model;

public class Usua {
    private static Usua instance;
    private String Usuarioini, Tipoini, Contrasenaini, Correoini, Nombreini, Telefonoini, Fotoini, Saldoini, Tipos_cli ;

    private Usua(){}

    public static synchronized Usua getInstance(){
        if (instance==null){
            instance =new Usua();
        }
        return instance;
    }
    public  String getTipoini(){
        return  Tipoini;
    }
    public void setTipoini(String tipoini){
        this.Tipoini=tipoini;
    }
    public String getUsuarioini(){
        return Usuarioini;
    }
    public  void setUsuarioini(String usuarioini){
        this.Usuarioini=usuarioini;
    }

    public String getContrasenaini() {
        return Contrasenaini;
    }

    public void setContrasenaini(String contrasenaini) {
        Contrasenaini = contrasenaini;
    }

    public String getCorreoini() {
        return Correoini;
    }

    public void setCorreoini(String correoini) {
        Correoini = correoini;
    }

    public String getNombreini() {
        return Nombreini;
    }

    public void setNombreini(String nombreini) {
        Nombreini = nombreini;
    }

    public String getTelefonoini() {
        return Telefonoini;
    }

    public void setTelefonoini(String telefonoini) {
        Telefonoini = telefonoini;
    }

    public String getFotoini() {
        return Fotoini;
    }

    public void setFotoini(String fotoini) {
        Fotoini = fotoini;
    }

    public String getSaldoini() {
        return Saldoini;
    }

    public void setSaldoini(String saldoini) {
        Saldoini = saldoini;
    }

    public String getTipos_cli() {
        return Tipos_cli;
    }

    public void setTipos_cli(String tipos_cli) {
        Tipos_cli = tipos_cli;
    }
}
