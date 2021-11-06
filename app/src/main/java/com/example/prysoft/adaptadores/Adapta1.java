package com.example.prysoft.adaptadores;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.prysoft.ActModAP;
import com.example.prysoft.ActModNOAP;
import com.example.prysoft.R;
import com.example.prysoft.beans.moduloClass;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adapta1 extends RecyclerView.Adapter<Adapta1.myHolder> {


    List<moduloClass> lista;
    List<moduloClass> lista1;
    Context contexto;
    String tipo;


    public Adapta1(List<moduloClass> lista, Context contexto) {
        this.lista = lista;
        this.contexto = contexto;
        lista1 = new ArrayList<>();
        lista1.addAll(lista);
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //relaciona con la vista1 controles hasta el adaptador
        LayoutInflater infla = LayoutInflater.from(contexto);
       View v = infla.inflate(R.layout.vista1,parent,false);
        return new myHolder(v);
    }


    //filtrado

    public void filtrado(String sBuscar){
        int longitud = sBuscar.length();
        if(longitud==0){
            lista.clear();
            lista.addAll(lista1);
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<moduloClass> collecion = lista.stream().filter(i -> i.getTitulo().
                        toLowerCase().contains(sBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                lista.clear();
                lista.addAll(collecion);
            }else{
                for (moduloClass l: lista1) {
                    if(l.getTitulo().toLowerCase().contains(sBuscar.toLowerCase())){
                        lista.add(l);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        //relacionar los datos con los controles del holder
        moduloClass mod = lista.get(position);
               holder.txtVistaTitulo.setText(mod.getTitulo());

             if(mod.getTipo()==1){
                 tipo="aprobado";
                 holder.txtVistaEstado.setText(tipo);
                 holder.txtVistaEstado.setTextColor(Color.GREEN);
             }else{
                 tipo="sin aprobar";
                 holder.txtVistaEstado.setText(tipo);
                 holder.txtVistaEstado.setTextColor(Color.RED);
             }


        holder.layout.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     if(mod.getTipo()==1) {
                         Intent intent = new Intent(contexto, ActModAP.class);
                         intent.putExtra("idMod", mod.getIdModulo());
                         Log.w("idMod", mod.getIdModulo());
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         contexto.startActivity(intent);
                     }else {
                         Intent intent = new Intent(contexto, ActModNOAP.class);
                         intent.putExtra("idMod", mod.getIdModulo());
                         Log.w("idMod", mod.getIdModulo());
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         contexto.startActivity(intent);
                     }
                 }
             });



    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        //variables para castear los controles con la lista
        TextView txtVistaTitulo,txtVistaEstado;
        LinearLayout layout;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            //relaciona los controles con la variable
            txtVistaTitulo= itemView.findViewById(R.id.txtVistaTitulo);
            txtVistaEstado = itemView.findViewById(R.id.txtVistaEstado);
            layout = itemView.findViewById(R.id.milayout);

        }
    }
}
