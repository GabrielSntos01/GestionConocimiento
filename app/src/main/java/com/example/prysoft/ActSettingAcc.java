package com.example.prysoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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

public class ActSettingAcc extends AppCompatActivity {

    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/Controla.php";
    //String url="http://10.0.2.2/pryGestion/Controla.php";
    JsonObjectRequest json;
    JSONArray vec;
    Button btnprocesar;
    TextView txtnom3,txtEditPass,txtcorreo;
    EditText edPassOld,edPassNew;
    Toolbar toolbar;
    String IdUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_setting_acc);


        IdUser = getIntent().getStringExtra("UserId");

        identificarusuario();

        toolbar = findViewById(R.id.toolbarsetting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("    Ajustes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(),ActAccount.class);
                startActivity(it);
                finish();
            }
        });



        txtnom3 = findViewById(R.id.txtNom3);
        txtcorreo = findViewById(R.id.txtCorreo);
        txtEditPass = findViewById(R.id.txtEditPass);
        edPassOld = findViewById(R.id.edPassOld);
        edPassNew =findViewById(R.id.edPassNew);
        btnprocesar = findViewById(R.id.btnProcesar1);

        edPassOld.setVisibility(View.INVISIBLE);
        edPassNew.setVisibility(View.INVISIBLE);
        btnprocesar.setVisibility(View.INVISIBLE);



        txtEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edPassNew.setVisibility(View.VISIBLE);
                edPassOld.setVisibility(View.VISIBLE);
                btnprocesar.setVisibility(View.VISIBLE);

            }
        });


        btnprocesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edPassOld.getText().toString().isEmpty() && edPassNew.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "Llenar los espacios requeridos...", Toast.LENGTH_SHORT).show();
                    return;
                }

                String enlace = url+"?tag=password&id="+IdUser+"&pold="+edPassOld.getText().toString()+"&pnew="+edPassNew.getText().toString();
                json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String cad = response.getString("dato");
                            Log.w("dato",cad);


                            if(cad.equals("error"))
                                Toast.makeText(getApplication(),"ERROR DE CONTRASEÑA", Toast.LENGTH_SHORT).show();

                            if(!cad.equals("error")){

                                Toast.makeText(getApplication(),"CONTRASEÑA CAMBIADA", Toast.LENGTH_SHORT).show();
                                edPassNew.setText("");
                                edPassOld.setText("");
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
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    public void identificarusuario(){

        String enlace = url+"?tag=usuario&id="+IdUser;

        json = new JsonObjectRequest(Request.Method.GET, enlace, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    vec = response.getJSONArray("dato");
                    Log.w("dato",vec.toString());

                    if(vec.length()>0){
                        JSONObject fila = (JSONObject)vec.get(0);

                        txtnom3.setText(fila.getString("Apellidos")+" "+fila.getString("Nombres"));
                        txtcorreo.setText(fila.getString("Email"));

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

        RequestQueue cola =Volley.newRequestQueue(this);
        cola.add(json);

    }



}