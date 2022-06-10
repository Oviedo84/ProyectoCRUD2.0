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

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder>{ //ojo en estos cambios
    private AdapterProducts.RecyclerViewClickListener listener;
    List<GetUsers> usuarios;
    LayoutInflater inflater;

    public interface RecyclerViewClickListener {
        public void recyclerViewClickListener(View v, int position);
    }
    public AdapterUsers (List<GetUsers> usuarios, Context mContext, AdapterProducts.RecyclerViewClickListener listener) {
        this.usuarios = usuarios;
        this.inflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_user, parent, false); //duda
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetUsers usuario = usuarios.get(position); //Cambiar a los atributos
        holder.nivel.setText(usuario.getNivel_id());
        holder.usuario.setText(usuario.getUsuario());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nivel, password, usuario;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nivel = (TextView) itemView.findViewById(R.id.nivel_id);
            usuario = (TextView) itemView.findViewById(R.id.usuario);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
