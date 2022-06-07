package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainProductos extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomnavigationview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_productos);
        bottomnavigationview = findViewById(R.id.bottomNavigationView);
        bottomnavigationview.setBackground(null);
        bottomnavigationview.getMenu().getItem(1).setEnabled(false);
        bottomnavigationview.getMenu().getItem(2).setEnabled(false);

        floatingActionButton = findViewById(R.id.fab);
        // Fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.load_fragment, new ListProducts()).commit();

        // Insert Product
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFAB();
                getSupportFragmentManager().beginTransaction().replace(R.id.load_fragment, new InsertProduct()).addToBackStack(null).commit();
            }
        });
    }

    public void showUpFAB(){
        floatingActionButton.show();
    }

    public void hideFAB(){
        floatingActionButton.hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainProductos activity = MainProductos.this;
        if(activity != null){
            activity.showUpFAB();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager mgr = getSupportFragmentManager();
        if (mgr.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            floatingActionButton.show();
            mgr.popBackStackImmediate();
        }
    }
}