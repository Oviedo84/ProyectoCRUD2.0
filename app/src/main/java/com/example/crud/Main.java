package com.example.crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton floatingActionButton;
    BottomNavigationView bottomnavigationview;
    NavigationView navigationView;
    int numActivity = 1;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Bottom Navigation View
        bottomnavigationview = findViewById(R.id.bottomNavigationView);
        bottomnavigationview.setBackground(null);
        bottomnavigationview.getMenu().getItem(1).setEnabled(false);
        bottomnavigationview.getMenu().getItem(2).setEnabled(false);
        bottomnavigationview.setOnItemSelectedListener(mOnItemSelectedListener);

        // Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Floating Action Button
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
                switch (numActivity) {
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.load_fragment, new InsertProduct()).addToBackStack(null).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.load_fragment, new EditProducts()).addToBackStack(null).commit();
                        break;
                }
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
        Main activity = Main.this;
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

    private final BottomNavigationView.OnItemSelectedListener mOnItemSelectedListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.miMenu:
                    mDrawerLayout.open();
                    return true;
                case R.id.miSearch:
                    Toast.makeText(Main.this, "Search Seleccionado", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.miFilter:
                    Toast.makeText(Main.this, "Filter Seleccionado", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_productos:
                numActivity = 1;
                Toast.makeText(Main.this, "Productos Seleccionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_categoria:
                numActivity = 2;
                Toast.makeText(Main.this, "Categoria Seleccionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_compras:
                numActivity = 3;
                Toast.makeText(Main.this, "Compras Seleccionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_usuarios:
                numActivity = 4;
                Toast.makeText(Main.this, "Usuarios Seleccionado", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}