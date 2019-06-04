package com.example.swpanicbutton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Inicio extends AppCompatActivity {

    SharedPreferences preferencias;
    String cadena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cadena=="unknown"){
                    Intent i = new Intent(Inicio.this ,Fromulario.class );
                    startActivity(i);}
                else{
                    Intent i = new Intent(Inicio.this ,Main2Activity.class );
                    startActivity(i);

                }

            }
        },1000);
    }
    protected void onResume(){
        super.onResume();

        preferencias = getSharedPreferences("Mis Preferencias", Context.MODE_PRIVATE);
        cadena = preferencias.getString("tel", "unknown");
    }
}
