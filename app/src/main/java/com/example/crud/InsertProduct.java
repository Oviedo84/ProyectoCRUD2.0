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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertProduct extends Fragment {
    private String products = "http://192.168.0.8:8080/insertProd";
    private String categorias = "http://192.168.0.8:8080/cat";

    private Button insertButton;
    private EditText insertNombre, insertDescripcion, insertPventa, insertPcompra, insertFecha, insertCantidad;
    RequestQueue requestQueue;
    EditText mDateFormat;
    List<GetCategories> getCategoriesList;
    String[] items = {"1", "0"};
    AutoCompleteTextView insertActivo;
    AutoCompleteTextView insertCategoria;
    ArrayAdapter<String> adapterCategorias;
    ArrayAdapter<String> adapterActivo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_product, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());

        // Calendario
        mDateFormat = (EditText) view.findViewById(R.id.fechaFormat);
        initCalendar();

        // Insertar Producto
        insertNombre = (EditText) view.findViewById(R.id.nombre);
        insertDescripcion = (EditText) view.findViewById(R.id.descripcion);
        insertPventa = (EditText) view.findViewById(R.id.precio_venta);
        insertPcompra = (EditText) view.findViewById(R.id.precio_compra);
        insertFecha = (EditText) view.findViewById(R.id.fechaFormat);
        insertCantidad = (EditText) view.findViewById(R.id.cantidad);
        insertActivo = (AutoCompleteTextView) view.findViewById(R.id.activo);
        insertCategoria = (AutoCompleteTextView) view.findViewById(R.id.categoria);

        // Selection Categoria
        CategorySelection();

        // Selection Activo
        ActiveSelection();

        insertButton = (Button) view.findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insert()){
                    Fragment main = new ListProducts();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.load_fragment, main);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    // Mostrar el FAB
                    MainProductos activity = (MainProductos)getActivity();
                    activity.showUpFAB();
                }
            }
        });
    }

    void initCalendar(){
        // Inicializar el Calendario
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = format.format(calendar.getTime());
        mDateFormat.setText(formattedDate);
        // EditText no editable mediante teclado
        mDateFormat.setFocusable(false);
        mDateFormat.setClickable(true);

        // Configuracion del datepicker de Material
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        mDateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getActivity().getSupportFragmentManager(), "Material_Date_Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(selection);
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String formattedDate = format.format(calendar.getTime());
                        mDateFormat.setText(formattedDate);
                    }
                });
            }
        });
    }

    boolean insert(){
        String nombre = insertNombre.getText().toString().trim();
        String Descripcion = insertDescripcion.getText().toString().trim();
        String Pventa = insertPventa.getText().toString().trim();
        String Pcompra = insertPcompra.getText().toString().trim();
        String Fecha = insertFecha.getText().toString().trim();
        String Cantidad = insertCantidad.getText().toString().trim();
        String Activo = insertActivo.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if (nombre.isEmpty()) {
            insertNombre.setError("Complete el campo");
            return false;
        } else if (Descripcion.isEmpty()) {
            insertDescripcion.setError("Complete el campo");
            return false;
        } else if (Pventa.isEmpty()) {
            insertPventa.setError("Complete el campo");
            return false;
        } else if (Pcompra.isEmpty()) {
            insertPcompra.setError("Complete el campo");
            return false;
        } else if (Fecha.isEmpty()) {
            insertFecha.setError("Complete el campo");
            return false;
        } else if (Cantidad.isEmpty()) {
            insertCantidad.setError("Complete el campo");
            return false;
        } else if (Activo.isEmpty()) {
            insertActivo.setError("Complete el campo");
            return false;
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    products,
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
                    params.put("p_venta", Pventa);
                    params.put("p_compra", Pcompra);
                    params.put("fecha", Fecha);
                    params.put("activo", Activo);
                    params.put("cantidad", Cantidad);
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
        MainProductos activity = (MainProductos)getActivity();
        if (activity != null) {
            activity.hideFAB();
        }
    }

    private void getCategories(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                categorias,
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
                                getCategoriesList.add(new GetCategories(categoria_id, nombre, descripcion));
                                adapterCategorias.add(nombre);
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

    private void ActiveSelection(){
        adapterActivo = new ArrayAdapter<String>(getContext(),R.layout.list_product_active, items);
        insertActivo.setAdapter(adapterActivo);
        insertActivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });
    }

    private void CategorySelection(){
        getCategoriesList = new ArrayList<>();
        adapterCategorias = new ArrayAdapter<String>(getContext(),R.layout.list_product_active);
        this.getCategories();
        insertCategoria.setAdapter(adapterCategorias);
        insertCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });
    }
}