package com.example.crud;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterProducts extends RecyclerView.Adapter<AdapterProducts.ViewHolder>{
    private RecyclerViewClickListener listener;
    List<GetProducts> productos;
    LayoutInflater inflater;


    public AdapterProducts(List<GetProducts> productos, Context mContext, RecyclerViewClickListener listener) {
        this.productos = productos;
        this.inflater = LayoutInflater.from(mContext);
        this.listener = listener;
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

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombre, descripcion, precio;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.prodNombre);
            descripcion = (TextView) itemView.findViewById(R.id.prodCategoria);
            precio = (TextView) itemView.findViewById(R.id.prodPrecio);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
