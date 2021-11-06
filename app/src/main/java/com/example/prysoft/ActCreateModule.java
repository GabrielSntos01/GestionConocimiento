package com.example.prysoft;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prysoft.adaptadores.GridViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ActCreateModule extends AppCompatActivity {
    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;

    EditText edttitulo,edtexto;
    Button btnOp1,btnOp2;

    Toolbar toolbar;
    String id;
    String idModule;

    String TIPO1="1";
    String TIPO2="2";

    //imagenes
    int PICK_IMAGE=100;
    Uri imageUri;
    Button btnGaleria;
    GridView gvImagenes;

    List<Uri> listaImagenes = new ArrayList<>();
    List<String> listaBase64Imagenes = new ArrayList<>();
    GridViewAdapter baseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_create_module);

         id = getIntent().getStringExtra("UserId");

        gvImagenes = findViewById(R.id.gvImagenes);
        btnGaleria = findViewById(R.id.btnGaleria);
        toolbar  = findViewById(R.id.toolbarcreatemodule);
        edttitulo =findViewById(R.id.edModuleTitle);
        edtexto = findViewById(R.id.edModuleDescription);
        btnOp1 = findViewById(R.id.btnOP1);
        btnOp2 = findViewById(R.id.btnOP2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Módulo de Creación de Conocimiento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPreference();
                retornar();
            }
        });


        btnOp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtexto.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "Llenar los espacios requeridos...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(edttitulo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "Llenar los espacios requeridos...", Toast.LENGTH_SHORT).show();
                    return;
                }

                confirmarModulo();
                createOp1();


            }
        });


        btnOp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtexto.getText().toString().isEmpty() && edttitulo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "Llenar los espacios requeridos...", Toast.LENGTH_SHORT).show();
                    return;
                }

                    confirmarModulo();
                    createOp2();

            }

        });


        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               abrirGaleria();
            }
        });

    }

    public void subirImagenes(String idModule) {

        listaBase64Imagenes.clear();

        for(int i = 0 ; i < listaImagenes.size() ; i++) {
            try {
                InputStream is = getContentResolver().openInputStream(listaImagenes.get(i));
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                String cadena = convertirUriToBase64(bitmap);

                enviarImagenes(idModule+i, cadena,idModule);

                bitmap.recycle();

            } catch (IOException e) { }

        }
    }

    public void enviarImagenes(final String nombre, final String cadena,final String idModule) {

        String enlace=url+"?tag=imagenes";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,enlace,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("idMod",idModule);
                params.put("nom", nombre);
                params.put("cadena", cadena);
                return params;
            }
        };
        requestQueue.add(stringRequest);



    }

    public String convertirUriToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encode;
    }

    public void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"SELECCIONA LAS IMAGENES"),PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
        ClipData clipData = data.getClipData();

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {

            if(clipData == null) {
                imageUri = data.getData();
                listaImagenes.add(imageUri);

            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    listaImagenes.add(clipData.getItemAt(i).getUri());
                }
            }
        }
        }catch (Exception e){e.printStackTrace();}


        baseAdapter = new GridViewAdapter(ActCreateModule.this, listaImagenes);
        gvImagenes.setAdapter(baseAdapter);

    }

    public void createOp1(){
        bdModulo(TIPO1);
    }

    public void createOp2(){

        bdModulo(TIPO2);

    }

    public void bdModulo(String tipo){
        String enlace =url+"?tag=createmodule&iduser="+id+"&tipo="+tipo+"&titulo="+edttitulo.getText().toString()+"&texto="+edtexto.getText().toString();
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    vec = response.getJSONArray("dato");
                    Log.w("iDmodule1",vec.toString());
                    if(vec.length()>0){
                        JSONObject fila = (JSONObject)vec.get(0);
                        guardarPreference(fila.getString("IdModulo"));

                    }

                    idModule = cargarPreferencias();

                    subirImagenes(cargarPreferencias());

                    Log.w("idModule2",idModule);

                }catch (Exception ex){
                    Toast.makeText(getApplication(),"ERROR DE DATOS"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"ERROR DE ENLACE"+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(json);

    }


    public void confirmarModulo(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(ActCreateModule.this);
        alerta.setMessage("Confirmar creacion")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retornar();
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

    public void retornar(){

        Intent it = new Intent(getApplication(),ActAccount.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    public void guardarPreference(String idModule){
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencias.edit();

        editor.putString("idModule",idModule);

        editor.commit();

    }

    public String cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);

        idModule = preferences.getString("idModule","No hay datos");

        return idModule;
    }

    public void eliminarPreference(){

        getSharedPreferences("datos",Context.MODE_PRIVATE).edit().remove("idModule").apply();

    }


}