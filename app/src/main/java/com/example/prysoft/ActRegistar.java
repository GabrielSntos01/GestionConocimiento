package com.example.prysoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ActRegistar extends AppCompatActivity {
    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;
    EditText ednom,edape,edem,edpas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_registar);
        edape = findViewById(R.id.edApe);
        ednom = findViewById(R.id.edNom);
        edem = findViewById(R.id.edEm);
        edpas = findViewById(R.id.edPas);
    }

    public void btnRegistrar(View v){
        String ape_string=edape.getText().toString();
        String nom_string=ednom.getText().toString();
        String em_string=edem.getText().toString();
        String pas_string=edpas.getText().toString();

        if(ape_string.isEmpty()){
            edape.setError("Este campo esta vacio");
            return;
        }
        if(nom_string.isEmpty()){
            ednom.setError("Este campo esta vacio");
            return;
        }
        if(em_string.isEmpty()){
            edem.setError("Este campo esta vacio");
            return;
        }
        if(pas_string.isEmpty()){
            edpas.setError("Este campo esta vacio");
            return;
        }


        String enlace = url + "?tag=regis&ape="+ape_string+"&nom="+nom_string+"&em="+em_string+"&pass="+pas_string;
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             try {
                 String cad = response.getString("dato");
                 Log.w("dato",cad);

                     if (cad.equals("error")) {
                         Toast.makeText(getApplication(), "EL CORREO YA EXISTE", Toast.LENGTH_SHORT).show();
                         return;
                     }
                 edape.setText("");
                 ednom.setText("");
                 edem.setText("");
                 edpas.setText("");
                 Toast.makeText(getApplication(), "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                 return;


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

    public void btnRegresar(View v){
        Intent it = new Intent(getApplication(),ActLogin.class);
        startActivity(it);

    }

}