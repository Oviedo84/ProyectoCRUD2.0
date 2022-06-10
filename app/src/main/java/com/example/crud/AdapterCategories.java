package com.example.crud;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolder>{
    private AdapterProducts.RecyclerViewClickListener listener;
    List<GetCategories> categorias;
    LayoutInflater inflater;

    public interface RecyclerViewClickListener {
        public void recyclerViewClickListener(View v, int position);
    }
    public AdapterCategories(List<GetCategories> categorias, Context mContext, AdapterProducts.RecyclerViewClickListener listener) {
        this.categorias = categorias;
        this.inflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetCategories categoria = categorias.get(position);
        holder.nombre.setText(categoria.getNombre());
        holder.descripcion.setText(categoria.getDescripcion());

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nombre, descripcion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }
}
