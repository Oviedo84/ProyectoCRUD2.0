package com.example.crud;

import java.io.Serializable;

public class GetProducts {
    private String producto_id, nombre, descripcion, p_venta, p_compra, fecha, activo, cantidad;

    public GetProducts() { }

    public GetProducts(String producto_id, String nombre, String descripcion, String p_venta, String p_compra, String fecha, String activo, String cantidad) {
        this.producto_id = producto_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.p_venta = p_venta;
        this.p_compra = p_compra;
        this.fecha = fecha;
        this.activo = activo;
        this.cantidad = cantidad;
    }

    public String getProducto_id() {
        return producto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getP_venta() {
        return p_venta;
    }

    public String getP_compra() {
        return p_compra;
    }

    public String getFecha() {
        return fecha;
    }

    public String getActivo() {
        return activo;
    }

    public String getCantidad() {
        return cantidad;
    }

}
