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

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.ViewHolder>{
    private AdapterProducts.RecyclerViewClickListener listener;
    List<GetCompras> compras;
    LayoutInflater inflater;

    public interface RecyclerViewClickListener {
        public void recyclerViewClickListener(View v, int position);
    }

    public AdapterCompras(List<GetCompras> compras, Context mContext, AdapterProducts.RecyclerViewClickListener listener) {
        this.compras = compras;
        this.inflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_compra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetCompras compra = compras.get(position);
        holder.fecha.setText(compra.getFecha());
        holder.cantidad.setText(compra.getCantidad());
        holder.proveedor.setText(compra.getProveedor());
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView fecha, cantidad, proveedor;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.compFecha);
            cantidad = (TextView) itemView.findViewById(R.id.compCantidad);
            proveedor = (TextView) itemView.findViewById(R.id.compProveedor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
