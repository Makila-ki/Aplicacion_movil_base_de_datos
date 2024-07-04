package com.example.proyecto.model;

public class Pro_cli {
    String Nombre_pro,Tipo_pro, Cantidad_pro,Precio_pro, Estado, Photo ;

    public  Pro_cli(){
    }

    public Pro_cli(String nombre_pro, String tipo_pro, String cantidad_pro, String precio_pro, String estado, String photo) {
        Nombre_pro = nombre_pro;
        Tipo_pro = tipo_pro;
        Cantidad_pro = cantidad_pro;
        Precio_pro = precio_pro;
        Estado = estado;
        Photo = photo;
    }

    public String getNombre_pro() {
        return Nombre_pro;
    }

    public void setNombre_pro(String nombre_pro) {
        Nombre_pro = nombre_pro;
    }

    public String getTipo_pro() {
        return Tipo_pro;
    }

    public void setTipo_pro(String tipo_pro) {
        Tipo_pro = tipo_pro;
    }

    public String getCantidad_pro() {
        return Cantidad_pro;
    }

    public void setCantidad_pro(String cantidad_pro) {
        Cantidad_pro = cantidad_pro;
    }

    public String getPrecio_pro() {
        return Precio_pro;
    }

    public void setPrecio_pro(String precio_pro) {
        Precio_pro = precio_pro;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
