package com.example.prysoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prysoft.beans.LoginClass;

import org.json.JSONArray;
import org.json.JSONObject;


public class ActLogin extends AppCompatActivity {

    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;
    EditText edlog,edpass;
    TextView txtlog;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_login);
        edlog = findViewById(R.id.edEmail);
        edpass = findViewById(R.id.edPass);
        txtlog = findViewById(R.id.txtlog);

        txtlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(),ActRegistar.class);
                startActivity(it);
            }
        });

    }

    public void btnLog(View v ){


        String enlace =url+"?tag=login&user="+edlog.getText().toString()+"&pass="+edpass.getText().toString();
        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String cad = response.getString("dato");
                    Log.w("dato",cad);


                    if(cad.equals("error"))
                        Toast.makeText(getApplication(),"ERROR DE LOGIN", Toast.LENGTH_SHORT).show();

                    if(!cad.equals("error")){
                        vec = response.getJSONArray("dato");
                        Log.w("dato",vec.toString());
                          if(vec.length()>0) {
                              JSONObject fila = (JSONObject) vec.get(0);

                              id = fila.getString("id");
                          }
                        Intent it = new Intent(getApplication(),ActAccount.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        guardarPreference(id);
                        startActivity(it);

                    }


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

        RequestQueue cola = Volley.newRequestQueue(getApplication());
        cola.add(json);
    }



    public void guardarPreference(String UserId){
        SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencias.edit();

        editor.putString("UserId",UserId);

        editor.commit();

    }
}