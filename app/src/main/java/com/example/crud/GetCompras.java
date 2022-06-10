package com.example.crud;

public class GetCompras {
    private String compra_id, producto_id, usuario_id, fecha, cantidad, proveedor;

    public GetCompras() { }

    public GetCompras(String compra_id, String producto_id, String usuario_id, String fecha, String cantidad, String proveedor) {
        this.compra_id  = compra_id;
        this.producto_id = producto_id;
        this.usuario_id = usuario_id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }

    public String getCompra_id() {
        return compra_id;
    }

    public String getProducto_id() {
        return producto_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getProveedor() { return proveedor; }
}
