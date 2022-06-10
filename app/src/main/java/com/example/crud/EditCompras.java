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
import android.widget.TextView;
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

public class EditCompras extends Fragment {
    private String EditCompra;

    private Button insertButton;
    private EditText insertProducto_id, insertUsuario_id, insertFecha, insertCantidad, insertProveedor;
    RequestQueue requestQueue;
    EditText mDateFormat;
    List<GetCompras> getComprasList;
    ArrayAdapter<String> adapterItems;
    String producto_id, compra_id, usuario_id, cantidad, proveedor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_compras, container, false);
        Bundle bundle = this.getArguments();
        producto_id = bundle.getString("producto_id");
        compra_id = bundle.getString("compra_id");
        usuario_id = bundle.getString("usuario_id");
        cantidad = bundle.getString("cantidad");
        proveedor = bundle.getString("proveedor");
        EditCompra = "http://192.168.0.8:8080/editComp/" + compra_id;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());

        // Calendario
        mDateFormat = (EditText) view.findViewById(R.id.fechaFormat);
        initCalendar();

        // Insertar Compra
        insertProducto_id = (EditText) view.findViewById(R.id.producto_id);
        insertUsuario_id =  (EditText) view.findViewById(R.id.usuario_id);
        insertFecha = (EditText) view.findViewById(R.id.fechaFormat);
        insertCantidad = (EditText) view.findViewById(R.id.cantidad);
        insertProveedor = (EditText) view.findViewById(R.id.proveedor);

        insertProducto_id.setText(producto_id, TextView.BufferType.EDITABLE);
        insertUsuario_id.setText(usuario_id, TextView.BufferType.EDITABLE);
        insertCantidad.setText(cantidad, TextView.BufferType.EDITABLE);
        insertProveedor.setText(proveedor, TextView.BufferType.EDITABLE);

        insertButton = (Button) view.findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(insert()){
                    Fragment main = new ListCompras();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.load_fragment, main);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    // Mostrar el FAB
                    Main activity = (Main) getActivity();
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
        String Producto_id = insertProducto_id.getText().toString().trim();
        String Usuario_id = insertUsuario_id.getText().toString().trim();
        String Fecha = insertFecha.getText().toString().trim();
        String Cantidad = insertCantidad.getText().toString().trim();
        String Proveedor = insertProveedor.getText().toString().trim();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if (Producto_id.isEmpty()) {
            insertProducto_id.setError("Complete el campo");
            return false;
        } else if (Usuario_id.isEmpty()) {
            insertUsuario_id.setError("Complete el campo");
            return false;
        } else if (Fecha.isEmpty()) {
            insertFecha.setError("Complete el campo");
            return false;
        } else if (Cantidad.isEmpty()) {
            insertCantidad.setError("Complete el campo");
            return false;
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.PUT,
                    EditCompra,
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
                    params.put("producto_id", Producto_id);
                    params.put("usuario_id", Usuario_id);
                    params.put("fecha", Fecha);
                    params.put("cantidad", Cantidad);
                    params.put("proveedor", Proveedor);
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