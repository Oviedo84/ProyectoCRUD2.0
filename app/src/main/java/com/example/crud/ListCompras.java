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

public class ListCompras extends Fragment {
    private String compras = "http://192.168.0.8:8080/listComp";
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    GetCompras getCompras;
    List<GetCompras> mCompra;
    AdapterCompras adapter;
    TextView textView;
    AdapterProducts.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_compras, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCompra = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        this.getCompras();
        this.deleteCompras();
    }

    private void getCompras(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                compras,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.get(i).toString());
                                String compra_id = jsonObject.getString("compra_id");
                                String producto_id = jsonObject.getString("producto_id");
                                String usuario_id = jsonObject.getString("usuario_id");
                                String fecha = jsonObject.getString("fecha");
                                String cantidad = jsonObject.getString("cantidad");
                                String proveedor = jsonObject.getString("proveedor");
                                getCompras = new GetCompras(compra_id, producto_id, usuario_id, fecha, cantidad, proveedor);
                                mCompra.add(getCompras);
                                setOnClickListener();
                                recyclerView.setAdapter(adapter);
                                adapter = new AdapterCompras(mCompra, getContext(), listener);
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
                GetCompras aux = mCompra.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("producto_id", aux.getProducto_id());
                bundle.putString("compra_id", aux.getCompra_id());
                bundle.putString("usuario_id", aux.getUsuario_id());
                bundle.putString("fecha", aux.getFecha());
                bundle.putString("cantidad", aux.getCantidad());
                bundle.putString("proveedor", aux.getProveedor());
                Fragment main = new EditCompras();
                main.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.load_fragment, main);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
    }

    public void deleteCompras(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getBindingAdapterPosition();
                GetCompras a = mCompra.get(pos);
                String id = a.getCompra_id();
                eliminarCompra(id);
                mCompra.remove(viewHolder.getBindingAdapterPosition());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void eliminarCompra(String id) {
        String deleteCompra = "http://10.0.2.2:8080/deleteCompra/" + id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                deleteCompra,
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