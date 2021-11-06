package com.example.prysoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prysoft.adaptadores.Adapta1;

import com.example.prysoft.beans.moduloClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActAccount extends AppCompatActivity implements SearchView.OnQueryTextListener{
    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;

    moduloClass mod;
    Toolbar toolbar;
    SearchView sBuscar;

    RecyclerView recy;
    List<moduloClass> list;
    Adapta1 ad;

    TextView textView5;

    public static String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_account);

        recy = findViewById(R.id.recy1);
        toolbar = findViewById(R.id.toolbar);
        sBuscar = findViewById(R.id.sbuscar);
        textView5 = findViewById(R.id.textView5);

        textView5.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("    GC");


            moduloTop();

         UserId = cargarPreferencias();

         sBuscar.setOnQueryTextListener(this);

         sBuscar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if(hasFocus) {
                     moduloall();
                 }else{
                     moduloTop();
                 }
             }
         });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                eliminarPreference2();
                Intent it = new Intent(this,ActCreateModule.class);
                it.putExtra("UserId",UserId);
                startActivity(it);
                return true;

            case R.id.item2:
                Intent it2 = new Intent(this,ActSettingAcc.class);
                it2.putExtra("UserId",UserId);
                Log.w("UserId",UserId);
                startActivity(it2);
                onPause();
                return true;


            case R.id.item3:
                eliminarPreference();
                Intent it3 = new Intent(this,MainActivity.class);
                it3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it3);
                return true;


            default: return super.onOptionsItemSelected(item);
        }

    }


    public void moduloTop(){
        String enlace= url+"?tag=buscarTopModulo&opcion=top";
        list = new ArrayList<>();
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("dato",vec.toString());
                    for(int i=0;i<vec.length();i++){
                        mod = new moduloClass();
                        JSONObject fila = (JSONObject)vec.get(i);
                        mod.setIdModulo(fila.getString("IdModulo"));
                        mod.setTipo(fila.getInt("tipo"));
                        mod.setTitulo(fila.getString("titulo"));
                        mod.setTexto(fila.getString("texto"));

                        list.add(mod);
                    }


                    ad = new Adapta1(list,getApplication());
                    recy.setLayoutManager(new LinearLayoutManager(getApplication()));
                    recy.setAdapter(ad);

                }catch (Exception ex){
                    //Toast.makeText(getApplication(),"ERROR DE DATOS"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(),"ERROR DE enlace"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);
    }


    public void moduloall(){
        String enlace= url+"?tag=buscarTopModulo&opcion=all";
        list = new ArrayList<>();
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("dato2",vec.toString());
                    for(int i=0;i<vec.length();i++){
                        mod = new moduloClass();
                        JSONObject fila = (JSONObject)vec.get(i);
                        mod.setIdModulo(fila.getString("IdModulo"));
                        mod.setTipo(fila.getInt("tipo"));
                        mod.setTitulo(fila.getString("titulo"));
                        mod.setTexto(fila.getString("texto"));

                        list.add(mod);
                    }

                    ad = new Adapta1(list,getApplication());
                    recy.setLayoutManager(new LinearLayoutManager(getApplication()));
                    recy.setAdapter(ad);


                }catch (Exception ex){
                    //Toast.makeText(getApplication(),"ERROR DE DATOS"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(),"ERROR DE enlace"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);
    }


    public String cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        UserId = preferences.getString("UserId", "No hay dato");

        return UserId;
    }



    public void eliminarPreference(){

       getSharedPreferences("datos",Context.MODE_PRIVATE).edit().clear().apply();

    }

    public void eliminarPreference2(){

        getSharedPreferences("datos",Context.MODE_PRIVATE).edit().remove("idModule").apply();

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ad.filtrado(newText);
        return false;
    }
}