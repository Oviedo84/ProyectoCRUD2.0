package com.example.crud;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCategories extends Fragment {
    private String EditCat;

    private Button insertButton;
    private EditText insertNombre, insertDescripcion;
    RequestQueue requestQueue;
    List<GetCompras> getComprasList;
    ArrayAdapter<String> adapterItems;
    String categoria_id, nombre, descripcion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_categories, container, false);
        Bundle bundle = this.getArguments();
        categoria_id = bundle.getString("categoria_id");
        nombre = bundle.getString("nombre");
        descripcion = bundle.getString("descripcion");
        EditCat = "http://192.168.0.8:8080/editCat/" + categoria_id;
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());



        // Insertar Categorias
        insertNombre = (EditText) view.findViewById(R.id.nombre);
        insertDescripcion = (EditText) view.findViewById(R.id.descripcion);

        insertNombre.setText(nombre);
        insertDescripcion.setText(descripcion);

        insertButton = (Button) view.findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insert()){
                    Fragment main = new ListCategories();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.load_fragment, main);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    // Mostrar el FAB
                    Main activity = (Main)getActivity();
                    activity.showUpFAB();
                }
            }
        });
    }

    boolean insert(){
        String nombre = insertNombre.getText().toString().trim();
        String Descripcion = insertDescripcion.getText().toString().trim();


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if (nombre.isEmpty()) {
            insertNombre.setError("Complete el campo");
            return false;
        } else if (Descripcion.isEmpty()) {
            insertDescripcion.setError("Complete el campo");
            return false;
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.PUT,
                    EditCat,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            )
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>params=new HashMap<String, String>();
                    params.put("nombre", nombre);
                    params.put("descripcion", Descripcion);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Main activity = (Main) getActivity();
        if (activity != null) {
            activity.hideFAB();
        }
    }
}