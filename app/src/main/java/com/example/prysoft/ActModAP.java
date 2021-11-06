package com.example.prysoft;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.prysoft.adaptadores.Adapta2;
import com.example.prysoft.adaptadores.GridImageAdapter;

import com.example.prysoft.beans.comentClass;
import com.example.prysoft.beans.imagenClass;
import com.example.prysoft.beans.moduloClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActModAP extends AppCompatActivity {
    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;

    TextView txtTitulo,txtTexto,txtestadoap,txtautor;

    Toolbar toolbar;
    GridView grid1;
    List<imagenClass> list;
    List<comentClass> list2;
    String idMod;
    String idUser,idUserMod,tipo;
    RecyclerView recy;
    comentClass com;
    Button btnestado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_mod_ap);

        toolbar = findViewById(R.id.toolmodaprobado);
        grid1 = findViewById(R.id.grid1);
        txtTexto = findViewById(R.id.txtTextoAP);
        txtTitulo = findViewById(R.id.txtTituloAP);
        txtestadoap = findViewById(R.id.txtestadoap);
        txtautor = findViewById(R.id.txtAutornoap);
        recy =findViewById(R.id.recyComent);
        btnestado = findViewById(R.id.btnEstado);



        btnestado.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Módulo de Transformacion de Conocimiento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               retornarPrincipal();

            }
        });

        idMod = getIntent().getStringExtra("idMod");
        idUser = cargarPreferencias();

        bdtopcoment();
        conexMod(idMod);
        conexImg(idMod);



    }


    public void conexMod(String idMod){
        moduloClass mod = new moduloClass();
        String enlace = url+"?tag=buscarModulo&idMod="+idMod;
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("conexMod",vec.toString());
                    for(int i=0;i<vec.length();i++){
                       JSONObject fila = (JSONObject) vec.get(i);
                        mod.setTexto(fila.getString("texto"));
                        mod.setTipo(fila.getInt("tipo"));
                        mod.setTitulo(fila.getString("titulo"));
                        mod.setNom(fila.getString("Nombre"));
                        mod.setApe(fila.getString("Apellido"));
                        mod.setId(fila.getString("iduser"));
                    }

                    txtTexto.setText(mod.getTexto());
                    tipo = String.valueOf(mod.getTipo());
                    if(tipo.equals("1")){
                        String estado = "Aprobado";
                        txtestadoap.setText(estado);
                        txtestadoap.setTextColor(Color.GREEN);
                    }
                    txtTitulo.setText(mod.getTitulo());
                    txtTitulo.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    txtautor.setText(mod.getNom()+" "+mod.getApe());

                    if(mod.getId().equals(idUser)){
                        btnestado.setVisibility(View.VISIBLE);
                    }



                } catch (Exception ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);
    }

    public void conexImg(String idMod){
        String enlace = url+"?tag=buscarImagenes&idMod="+idMod;
        list = new ArrayList<>();
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("dato",vec.toString());
                    for(int i=0;i<vec.length();i++){
                        imagenClass img = new imagenClass();
                        JSONObject fila = (JSONObject) vec.get(i);
                        img.setIdImagen(fila.getString("IdImagen"));
                        img.setNombre(fila.getString("nombre"));

                        list.add(img);

                    }

                    GridImageAdapter gridImageAdapter = new GridImageAdapter(getApplication(),list);
                    grid1.setAdapter(gridImageAdapter);

                } catch (Exception ex) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);
    }


    public void btnComentar(View view){

        mostrarDialogoComentario();

    }

     private void mostrarDialogoComentario(){
         AlertDialog.Builder builder = new AlertDialog.Builder(ActModAP.this);
         LayoutInflater inflater = getLayoutInflater();
         View view = inflater.inflate(R.layout.cuadro_comentar,null);
         builder.setView(view);
         AlertDialog dialog = builder.create();
         dialog.show();
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

         EditText edComent=view.findViewById(R.id.edComentario);
         Button btncoment= view.findViewById(R.id.btnCuadrocoment);

         btncoment.setVisibility(View.INVISIBLE);


         edComent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if(hasFocus){
                     btncoment.setVisibility(View.VISIBLE);
                 }
             }
         });

         btncoment.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String edcoment = edComent.getText().toString();

                 if(edcoment.isEmpty()){
                     Toast.makeText(getApplication(),"Llenar comentario",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 bdcoment(edcoment);
                 Intent intent = new Intent(view.getContext(),ActModAP.class);
                 intent.putExtra("idMod",idMod);
                 bdtopcoment();
                 dialog.cancel();
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 startActivity(intent);
             }
         });

     }



    public void bdcoment(String texto){
        String enlace=url+"?tag=createcoment&idModulo="+idMod+"&idUsuario="+idUser+"&texto="+texto;
        json=new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                }catch (Exception ex){

                    Toast.makeText(getApplication(),"EEROR DE DATOS"+ex,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"EEROR DE ENLACE"+error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);

    }


    public void bdtopcoment(){
        String enlace=url+"?tag=topComent&idModulo="+idMod;
        list2 = new ArrayList<>();
        json=new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("dato",vec.toString());
                    for(int i=0;i<vec.length();i++){
                        com = new comentClass();
                        JSONObject fila = (JSONObject)vec.get(i);
                        com.setComentario(fila.getString("texto"));
                        com.setNom(fila.getString("Nombres"));
                        com.setApe(fila.getString("Apellidos"));
                        list2.add(com);
                    }

                    Adapta2 ad = new Adapta2(list2,getApplication());
                    recy.setLayoutManager(new LinearLayoutManager(getApplication()));
                    recy.setAdapter(ad);


                }catch (Exception ex){

                    //Toast.makeText(getApplication(),"EEROR DE DATOS"+ex,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(),"EEROR DE ENLACE"+error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);

    }

    public void btnestado(View view){
        confirmarNuevoEstado();
    }

    public void bdEstado(){
        tipo= "2";
        String enlace = url+"?tag=updateestado&tipo="+tipo+"&idMod="+idMod;
        json=new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                }catch (Exception ex){


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(),"EEROR DE ENLACE"+error,Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);



    }

    public void confirmarNuevoEstado(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(ActModAP.this);
        alerta.setMessage("Confirmar cambio de estado")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            bdEstado();
                            retornarPrincipal();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Confirmar");
        titulo.show();

    }


    public void retornarPrincipal(){
        Intent intent = new Intent(getApplication(),ActAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public String cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        idUser = preferences.getString("UserId", "No hay dato");

        return idUser;
    }




}