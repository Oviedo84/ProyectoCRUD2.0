package com.example.crud;

public class GetCategories {
    private String categoria_id, nombre, descripcion;

    public GetCategories() { }

    public GetCategories(String categoria_id, String nombre, String descripcion) {
        this.categoria_id = categoria_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
