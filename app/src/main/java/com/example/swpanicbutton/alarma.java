package com.example.swpanicbutton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class alarma extends AppCompatActivity {
    SharedPreferences preferencias;
    Button Guardar;
    String txt;
    EditText IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        Guardar = (Button)findViewById(R.id.btnGuardarIP);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEditor = preferencias.edit();

                txt = ((EditText)findViewById(R.id.editIP)).getText().toString();
                myEditor.putString("ip",txt);
                myEditor.commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(alarma.this ,Main2Activity.class );
                        startActivity(i);
                    }
                },200);
            }
        });
    }
    protected void onResume() {
        super.onResume();
        preferencias = getSharedPreferences("Mis Preferencias", Context.MODE_PRIVATE);
        IP = (EditText) findViewById(R.id.editIP);
        IP.setText(preferencias.getString("ip", "").toString(), TextView.BufferType.EDITABLE);

    }
}
