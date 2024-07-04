package com.example.proyecto.model;

public class Clien_car {
    String Nombre, Cantidad, Precio, Foto, Estado;
    public Clien_car(){

    }

    public Clien_car(String nombre, String cantidad, String precio, String foto, String estado) {
        Nombre = nombre;
        Cantidad = cantidad;
        Precio = precio;
        Foto = foto;
        Estado = estado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
}
