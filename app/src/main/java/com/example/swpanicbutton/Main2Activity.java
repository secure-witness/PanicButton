package com.example.swpanicbutton;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    ImageButton btnSms;
    SharedPreferences preferencias;
    TextView mensaje1;
    TextView mensaje2;
    String direccion,IP,dircasa,dirtrabajo,nombre;
    private static String longitud, latitud;

    public static String getLongitud(){
        return longitud;
    }

    public static String getLatitud(){
        return latitud;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        btnSms = (ImageButton)findViewById(R.id.imageButton3);
        mensaje1 = (TextView) findViewById(R.id.mensaje_id);
        mensaje2 = (TextView) findViewById(R.id.mensaje_id2);

        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    String url = "http://"+IP+"/led1";
                    //new SolicitaDatos().execute(url);
                    //v.setEnabled(false);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (responseBody != null) {
                                Toast.makeText(Main2Activity.this,"Alarma!",Toast.LENGTH_SHORT).show();
                            }

                            //v.setEnabled(true);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Toast.makeText(Main2Activity.this,"Error de conexión",Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(Main2Activity.this,"entrando",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Main2Activity.this,"Sin Internet",Toast.LENGTH_LONG).show();
                }
                nombre=preferencias.getString("nombre", "");
                dircasa="Calle: "+ preferencias.getString("callec", "unknown") + " # "+ preferencias.getString("numc", "unknown")+
                        " CP "+ preferencias.getString("cpc", "unknown") + " Del. " + preferencias.getString("delc", "unknown");
                dirtrabajo="Calle: "+ preferencias.getString("callet", "unknown") + " # "+ preferencias.getString("numt", "unknown")+
                        " CP "+ preferencias.getString("cpt", "unknown") + " Del. " + preferencias.getString("delt", "unknown");
                String mens1 = "Dirección de Casa" + "\n" + dircasa;

                //String mens2 = "Dirección de Trabajo" + "\n" + dirtrabajo;
                String mens2 = "long " + longitud + "lat " + latitud;

                String tel= preferencias.getString("tel", "unknown").toString();

                try{
                    SmsManager smgr = SmsManager.getDefault();
                    /*smgr.sendTextMessage(tel,null,"Alerta de Panico de "+ nombre + "\n" + direccion,null,null);
                    smgr.sendTextMessage(tel,null,"https://www.google.com/maps/search/?api=1&query="+latitud+","+longitud,null,null);
                    smgr.sendTextMessage(tel,null,mens1,null,null);
                    smgr.sendTextMessage(tel,null, mens2,null,null);*/
                    smgr.sendTextMessage(tel,null,mens2,null,null);
                    Toast.makeText(Main2Activity.this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    Toast.makeText(Main2Activity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }

               // new Insertar(Main2Activity.this).execute();
            }
        });

        ////////////////////////// Solicitud de permisos SMS ////////////////////////////////////////////////////////////////////


        if(ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Main2Activity.this,new String[]
                    { Manifest.permission.SEND_SMS,},1000);
        }else{
        };

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////777

                ///////////////////////////Solicitud de permisos LOCATION ////////////////////////////////////////////////////77



        if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else {
            locationStart();
        }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////7

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Perfil) {
            Intent i = new Intent(Main2Activity.this ,Fromulario.class );
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_wifi) {
            Intent i = new Intent(Main2Activity.this ,alarma.class );
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /////////////////////////////permisos de localizacion //////////////////////////////////////////
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////Direccion//////////////////////////////////77
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = DirCalle.getAddressLine(0);
                    mensaje2.setText(direccion);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// CLASE LOCALIZACION ///////////////////////////////////////7
    public class Localizacion implements LocationListener {
        Main2Activity main2Activity;
        public Main2Activity getMain2Activity() {
            return main2Activity;
        }
        public void setMainActivity(Main2Activity main2Activity) {
            this.main2Activity = main2Activity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            longitud = Double.toString(loc.getLongitude());
            latitud = Double.toString(loc.getLatitude());
            String Text = "Lat = " + latitud + "  Long = " + longitud;
            mensaje1.setText(Text);
            this.main2Activity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////LOCALIZACION//////////////////////////////////////////
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        mensaje1.setText("Localización agregada");
        mensaje2.setText("");
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    protected void onResume(){
        super.onResume();
        preferencias = getSharedPreferences("Mis Preferencias", Context.MODE_PRIVATE);
        IP=preferencias.getString("ip", "").toString();
        if(IP != ""){
            String url = "http://" + IP + "/probe";
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (responseBody != null) {
                        Toast.makeText(Main2Activity.this,new String(responseBody),Toast.LENGTH_SHORT).show();
                    }
                    //v.setEnabled(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    Toast.makeText(Main2Activity.this,"Error de conexión",Toast.LENGTH_SHORT).show();
                }
            });
            //new SolicitaDatos().execute(url);
        } else
        {
            Toast.makeText(Main2Activity.this,"Configure IP de alarma",Toast.LENGTH_LONG).show();

        }
    }
    ///////////////////////////////////////////////////////////////////////////
    private class SolicitaDatos extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            return Conexion.getDados(url[0]);
        }

        protected void onPostExecute(String resultado){

            if(resultado != null) {

                Toast.makeText(Main2Activity.this,"Conectado",Toast.LENGTH_LONG).show();

            }
            else{

                Toast.makeText(Main2Activity.this,"Errores de Conexión",Toast.LENGTH_LONG).show();

            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////Clase insertar datos metodo POST ////////////////////////////////////
    public boolean insertar() {
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost("http://192.168.100.217/webapp/alerta.php");
        //Se añaden los datos de la alerta
        nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("longitud", longitud));
        nameValuePairs.add(new BasicNameValuePair("latitud", latitud));
        nameValuePairs.add(new BasicNameValuePair("nombre", nombre));
        nameValuePairs.add(new BasicNameValuePair("direccion", direccion));
        nameValuePairs.add(new BasicNameValuePair("dircasa", dircasa));
        nameValuePairs.add(new BasicNameValuePair("dirtrabajo", dirtrabajo));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////CONSULTA ASINCRONA PARA AGREGAR DATOS A BD ////////////////////////////////
    class Insertar extends AsyncTask<String,String,String>{

        private Activity context;

        Insertar(Activity context){
            this.context=context;
        }
        @Override
        protected String doInBackground(String... params) {
            /* TODO Auto-generated method stub */
            if(insertar())
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Alerta en BD", Toast.LENGTH_LONG).show();

                    }
                });
            else
                context.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "No se capturo alerta", Toast.LENGTH_LONG).show();
                    }
                });
            return null;
        }
    }



}
