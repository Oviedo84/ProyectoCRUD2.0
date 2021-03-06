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

public class InsertCategories extends Fragment {
    private String categorias = "http://192.168.0.8:8080/cat";

    private Button insertButton;
    private EditText insertNombre, insertDescripcion;
    RequestQueue requestQueue;
    //EditText mDateFormat;
    List<GetCategories> getCategoriesList;
   // String[] items = {"1", "0"};
    //AutoCompleteTextView insertActivo;
   // ArrayAdapter<String> adapterItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_categories, container, false);
        return view; //ojo aqui con el cambio del nombre del archivo del layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());



        // Insertar Categorias
        insertNombre = (EditText) view.findViewById(R.id.nombre);
        insertDescripcion = (EditText) view.findViewById(R.id.descripcion);
       // insertPventa = (EditText) view.findViewById(R.id.precio_venta);
       // insertPcompra = (EditText) view.findViewById(R.id.precio_compra);
        // insertFecha = (EditText) view.findViewById(R.id.fechaFormat);
        //insertCantidad = (EditText) view.findViewById(R.id.cantidad);
       // insertActivo = (AutoCompleteTextView) view.findViewById(R.id.activo);

       // adapterItems = new ArrayAdapter<String>(getContext(),R.layout.list_product_active,items);
       // insertActivo.setAdapter(adapterItems);
       // insertActivo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       //     @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
        //});

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

    //void initCalendar(){
        // Inicializar el Calendario
       // Calendar calendar = Calendar.getInstance();
       // SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        //String formattedDate = format.format(calendar.getTime());
        //mDateFormat.setText(formattedDate);
        // EditText no editable mediante teclado
       // mDateFormat.setFocusable(false);
       // mDateFormat.setClickable(true);

        // Configuracion del datepicker de Material
        //MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
           //     .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
       // mDateFormat.setOnClickListener(new View.OnClickListener() {
       /*     @Override
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
        });*/
    //}

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
                    Request.Method.POST,
                    categorias,
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