package com.example.prysoft;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
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
import com.example.prysoft.beans.imagenClass;
import com.example.prysoft.beans.moduloClass;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ActModNOAP extends AppCompatActivity {

    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;

    Toolbar toolbar;
    TextView txtestadoNOAP,txtautornoap;
    EditText edTituloNOAP,edTextNOAP;

    imagenClass img;

    String idMod,nombreimage;

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
        setContentView(R.layout.activity_act_mod_noap);

        toolbar = findViewById(R.id.toolmodnoaprobado);
        txtestadoNOAP = findViewById(R.id.txtEstadoNOAP);
        edTextNOAP = findViewById(R.id.edTextoNOAP);
        edTituloNOAP = findViewById(R.id.edTituloNOAP);
        btnGaleria = findViewById(R.id.btnImagenesNOAP);
        gvImagenes=findViewById(R.id.gvImagenesNOAP);
        txtautornoap =findViewById(R.id.txtAutornoap);

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
        buscarModuloNoAP(idMod);
        bdlastnameimage();

        nombreimage = bdlastnameimage();

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });


    }

    public void btneditar(View view){

      String titulo = edTituloNOAP.getText().toString();
      String texto = edTextNOAP.getText().toString();

        if(titulo.isEmpty()){
            Toast.makeText(getApplication(),"Llenar los campos...",Toast.LENGTH_SHORT).show();
            return;
        }
        if(texto.isEmpty()){
            Toast.makeText(getApplication(),"Llenar los campos...",Toast.LENGTH_SHORT).show();
            return;
        }

        confirmarEdit(titulo,texto);


    }

    public void btnestado(View view){

        confirmarNuevoEstado();

    }

    public void bdestado(){
        String tipo= "1";
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

    public String bdlastnameimage(){
        String enlace = url+"?tag=lastnameImage&idMod="+idMod;
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("datoimagen",vec.toString());
                    if(vec.length()>0){
                        JSONObject fila = (JSONObject)vec.get(0);
                        nombreimage = fila.getString("nombre");

                    }
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

        return(nombreimage);
    }

    public void bdUpdateMOD(String titulo,String texto) {

        String enlace=url+"?tag=updateModNOAP";

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
                params.put("titulo",titulo);
                params.put("texto",texto);
                params.put("idMod",idMod);
                return params;
            }
        };
        requestQueue.add(stringRequest);



    }



    public void buscarModuloNoAP(String idMod){
        String enlace = url+"?tag=buscarModuloNoAP&idMod="+idMod;
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vec = response.getJSONArray("dato");
                    Log.w("dato",vec.toString());
                    for(int i=0;i<vec.length();i++){
                        img = new imagenClass();
                        JSONObject fila = (JSONObject)vec.get(i);
                        img.setTexto(fila.getString("texto"));
                        img.setTipo(fila.getInt("tipo"));
                        img.setTitulo(fila.getString("titulo"));
                        img.setNombre(fila.getString("Nombre"));
                        img.setApe(fila.getString("Apellido"));
                        img.setId(fila.getString("iduser"));

                    }

                    if(img.getTipo()==2) {
                        String estado = "sin aprobar";
                        txtestadoNOAP.setText(estado);
                        txtestadoNOAP.setTextColor(Color.RED);
                        edTituloNOAP.setText(img.getTitulo());
                        edTextNOAP.setText(img.getTexto());
                    }

                    txtautornoap.setText(img.getNombre()+" "+img.getApe());
                    txtautornoap.setTextColor(Color.BLUE);

                }catch (Exception ex){

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


    public void subirImagenes() {

        listaBase64Imagenes.clear();

        for(int i = 0 ; i < listaImagenes.size() ; i++) {
            try {
                InputStream is = getContentResolver().openInputStream(listaImagenes.get(i));
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                String cadena = convertirUriToBase64(bitmap);


                    if(nombreimage.equals("error")) {

                        enviarImagenes(idMod+i, cadena, idMod);
                    }else{
                        String cadenastring = nombreimage.substring(0,7);
                        String cadenaint = nombreimage.substring(7,9);
                        int nombre_int = Integer.parseInt(cadenaint);
                        int nombreimagen = nombre_int+1;
                        enviarImagenes(cadenastring+nombreimagen, cadena, idMod);
                    }

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
                params.put("nom", nombre);
                params.put("idMod",idModule);
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


        baseAdapter = new GridViewAdapter(ActModNOAP.this, listaImagenes);
        gvImagenes.setAdapter(baseAdapter);

    }


    public void confirmarNuevoEstado(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(ActModNOAP.this);
        alerta.setMessage("Confirmar cambio de estado")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bdestado();
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

    public void confirmarEdit(String titulo,String texto){
        AlertDialog.Builder alerta = new AlertDialog.Builder(ActModNOAP.this);
        alerta.setMessage("Guardar cambios")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bdUpdateMOD(titulo,texto);
                        subirImagenes();
                        retornarPrincipal();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alerta.create();
        alertDialog.setTitle("Confirmar");
        alertDialog.show();

    }


    public void retornarPrincipal(){
        Intent intent = new Intent(this,ActAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}