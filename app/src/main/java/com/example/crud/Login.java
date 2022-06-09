package com.example.crud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

public class Login extends AppCompatActivity {
    Button button;
    private String users = "http://192.168.0.8:8080/getUsers";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.init();
    }

    private void init(){
        button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPage(1);
            }
        });
    }

    private void mainPage(int privileges){
        if(privileges == 1){
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }
    }
}