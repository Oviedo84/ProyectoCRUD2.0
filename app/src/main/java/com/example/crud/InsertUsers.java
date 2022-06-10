package com.example.crud;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertUsers extends Fragment {
    private String users = "http://192.168.0.8:8080/cat";

    private Button insertButton;
    private EditText insertNivel, insertPassword, insertUsuario;
    RequestQueue requestQueue;
    List<GetUsers> getUserList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_users, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());


        // Insertar Usuario
        insertNivel = (EditText) view.findViewById(R.id.nivel);
        insertPassword = (EditText) view.findViewById(R.id.password);
        insertUsuario = (EditText) view.findViewById(R.id.usuario);



        insertButton = (Button) view.findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insert()){
                    Fragment main = new ListUsers();
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
        String nivel = insertNivel.getText().toString().trim();
        String password = insertPassword.getText().toString().trim();
        String usuario = insertUsuario.getText().toString().trim();


        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if (nivel.isEmpty()) {
            insertNivel.setError("Complete el campo");
            return false;
        } else if (password.isEmpty()) {
            insertPassword.setError("Complete el campo");
            return false;
        } else if (usuario.isEmpty()) {
            insertUsuario.setError("Complete el campo");
            return false;
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    users,
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
                    params.put("nivel", nivel);
                    params.put("password", password);
                    params.put("usuario", usuario);

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
        Main activity = (Main)getActivity();
        if (activity != null) {
            activity.hideFAB();
        }
    }
}