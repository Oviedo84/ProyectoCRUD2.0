package com.example.crud;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    Activity activity;
    List<GetProducts> productos;
    LayoutInflater inflater;

    public interface RecyclerViewClickListener {
        public void recyclerViewClickListener(View v, int position);
    }

    public Adapter(List<GetProducts> productos, Context mContext) {
        this.productos = productos;
        this.inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetProducts producto = productos.get(position);
        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getDescripcion());
        holder.precio.setText(producto.getP_venta());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, descripcion, precio;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.prodNombre);
            descripcion = (TextView) itemView.findViewById(R.id.prodCategoria);
            precio = (TextView) itemView.findViewById(R.id.prodPrecio);
        }
    }
}
