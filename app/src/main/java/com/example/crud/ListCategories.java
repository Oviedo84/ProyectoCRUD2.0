package com.example.crud;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListCategories extends Fragment {
    private String categories = "http://192.168.0.8:8080/cat";
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    GetCategories getCategories;
    List<GetCategories> cCategoria;
    AdapterCategories adapterCategories;
    TextView textView;
    AdapterProducts.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cCategoria = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        this.getCategories();
        this.deleteCategories();
    }

    private void getCategories(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, //Obtengo toda la informacion
                categories,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.get(i).toString());
                                String categoria_id = jsonObject.getString("categoria_id");
                                String nombre = jsonObject.getString("nombre");
                                String descripcion = jsonObject.getString("descripcion");
                                getCategories = new GetCategories(categoria_id, nombre, descripcion);
                                cCategoria.add(getCategories);
                                setOnClickListener();
                                recyclerView.setAdapter(adapterCategories);
                                adapterCategories = new AdapterCategories(cCategoria, getContext(), listener);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(arrayRequest);
    }

    void setOnClickListener() {
        listener = new AdapterProducts.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                GetCategories aux = cCategoria.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("categoria_id", aux.getCategoria_id());
                bundle.putString("nombre", aux.getNombre());
                bundle.putString("descripcion", aux.getDescripcion());
                Fragment main = new EditCategories();
                main.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.load_fragment, main);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
    }

    public void deleteCategories(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getBindingAdapterPosition();
                GetCategories c = cCategoria.get(pos);
                String id = c.getCategoria_id();
                eliminarCategoria(id);
                cCategoria.remove(viewHolder.getBindingAdapterPosition());
                recyclerView.setAdapter(adapterCategories);
                adapterCategories.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void eliminarCategoria(String id) {
        String deleteCategoria = "http://192.168.0.8:8080/deleteProduct/" + id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                deleteCategoria,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(stringRequest);
    }
}
