package com.example.swpanicbutton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fromulario extends AppCompatActivity {
    SharedPreferences preferencias;
Button Guardar;
String txt,nombre,callec,numc,cpc,delc,callet,numt,cpt,delt,tel,mens;
EditText Nombre,Callec,Numc,Cpc,Delc,Callet,Numt,Cpt,Delt,Tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fromulario);
        Guardar = (Button)findViewById(R.id.btnGuardar);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEditor = preferencias.edit();

                txt = ((EditText)findViewById(R.id.editNombreC)).getText().toString();
                myEditor.putString("nombre",txt);

                txt = ((EditText)findViewById(R.id.editCalleC)).getText().toString();
                myEditor.putString("callec",txt);
                txt = ((EditText)findViewById(R.id.editNumC)).getText().toString();
                myEditor.putString("numc",txt);
                txt = ((EditText)findViewById(R.id.editNCPC)).getText().toString();
                myEditor.putString("cpc",txt);
                txt = ((EditText)findViewById(R.id.editDelC)).getText().toString();
                myEditor.putString("delc",txt);


                txt = ((EditText)findViewById(R.id.editCalleT)).getText().toString();
                myEditor.putString("callet",txt);
                txt = ((EditText)findViewById(R.id.editNumT)).getText().toString();
                myEditor.putString("numt",txt);
                txt = ((EditText)findViewById(R.id.editNCPT)).getText().toString();
                myEditor.putString("cpt",txt);
                txt = ((EditText)findViewById(R.id.editDelT)).getText().toString();
                myEditor.putString("delt",txt);

                txt = ((EditText)findViewById(R.id.ediTel)).getText().toString();
                myEditor.putString("tel",txt);
                myEditor.commit();

                nombre=preferencias.getString("nombre", "").toString();
                callec=preferencias.getString("callec", "").toString();
                numc=preferencias.getString("numc", "").toString();
                cpc=preferencias.getString("cpc", "").toString();
                delc=preferencias.getString("delc", "").toString();

                callet=preferencias.getString("callet", "").toString();
                numt=preferencias.getString("numt", "").toString();
                cpt=preferencias.getString("cpt", "").toString();
                delt=preferencias.getString("delt", "").toString();
                tel=preferencias.getString("tel", "").toString();
                try{
                SmsManager smgr = SmsManager.getDefault();
                mens = "*p"+ nombre +"*c"+ callec+"*n"+numc+"*cp"+cpc+"*d"+delc;
                smgr.sendTextMessage(tel,null,mens,null,null);

                mens = "*t"+callet+"*n"+numt+"*cp"+cpt+"*d"+delt;
                smgr.sendTextMessage(tel,null,mens,null,null);

                }catch (Exception e){}


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(Fromulario.this ,Main2Activity.class );
                        startActivity(i);
                    }
                },200);
            }
        });
    }
    protected void onResume() {
        super.onResume();
        preferencias = getSharedPreferences("Mis Preferencias", Context.MODE_PRIVATE);
        Nombre = (EditText) findViewById(R.id.editNombreC);
        Nombre.setText(preferencias.getString("nombre", "").toString(), TextView.BufferType.EDITABLE);
        Callec = (EditText) findViewById(R.id.editCalleC);
        Callec.setText(preferencias.getString("callec", "").toString(), TextView.BufferType.EDITABLE);
        Numc = (EditText) findViewById(R.id.editNumC);
        Numc.setText(preferencias.getString("numc", "").toString(), TextView.BufferType.EDITABLE);
        Cpc = (EditText) findViewById(R.id.editNCPC);
        Cpc.setText(preferencias.getString("cpc", "").toString(), TextView.BufferType.EDITABLE);
        Delc = (EditText) findViewById(R.id.editDelC);
        Delc.setText(preferencias.getString("delc", "").toString(), TextView.BufferType.EDITABLE);

        Callet = (EditText) findViewById(R.id.editCalleT);
        Callet.setText(preferencias.getString("callet", "").toString(), TextView.BufferType.EDITABLE);
        Numt = (EditText) findViewById(R.id.editNumT);
        Numt.setText(preferencias.getString("numt", "").toString(), TextView.BufferType.EDITABLE);
        Cpt = (EditText) findViewById(R.id.editNCPT);
        Cpt.setText(preferencias.getString("cpt", "").toString(), TextView.BufferType.EDITABLE);
        Delt = (EditText) findViewById(R.id.editDelT);
        Delt.setText(preferencias.getString("delt", "").toString(), TextView.BufferType.EDITABLE);
        Tel = (EditText) findViewById(R.id.ediTel);
        Tel.setText(preferencias.getString("tel", "").toString(), TextView.BufferType.EDITABLE);
    }
}
