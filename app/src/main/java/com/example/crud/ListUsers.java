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

public class ListUsers extends Fragment {
    private String users = "http://192.168.0.8:8080/listUsers";
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    GetUsers getUsers;
    List<GetUsers> uUsers;
    AdapterUsers adapter;
    AdapterProducts.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_users, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uUsers = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        this.getUsers();
        this.deleteUsers();
    }

    private void getUsers(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET, //Obtengo toda la informacion
                users,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.get(i).toString());
                                String usuario_id = jsonObject.getString("usuario_id");
                                String nivel_id = jsonObject.getString("nivel_id");
                                String password = jsonObject.getString("password");
                                String usuario = jsonObject.getString("usuario");
                                getUsers = new GetUsers(usuario_id, nivel_id, password, usuario);
                                uUsers.add(getUsers);
                                setOnClickListener();
                                recyclerView.setAdapter(adapter);
                                adapter = new AdapterUsers(uUsers, getContext(), listener);
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
                GetUsers aux = uUsers.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("usuario_id", aux.getUsuario_id());
                bundle.putString("nivel_id", aux.getNivel_id());
                bundle.putString("password", aux.getPassword());
                bundle.putString("usuario", aux.getUsuario());
                Fragment main = new EditUsers();
                main.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.load_fragment, main);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
    }

    public void deleteUsers(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getBindingAdapterPosition();
                GetUsers u = uUsers.get(pos);
                String id = u.getNivel_id();
                eliminarUsuario(id);
                uUsers.remove(viewHolder.getBindingAdapterPosition());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void eliminarUsuario(String id) {
        String deleteUser = "http://192.168.0.8:8080/deleteProduct/" + id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                deleteUser,
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
