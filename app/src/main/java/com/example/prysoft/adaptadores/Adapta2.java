package com.example.prysoft.adaptadores;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prysoft.R;
import com.example.prysoft.beans.comentClass;


import java.util.List;

public class Adapta2 extends RecyclerView.Adapter<Adapta2.myHolder> {

   List<comentClass> lista;
    Context contexto;

    public Adapta2(List<comentClass> lista, Context contexto) {
        this.lista = lista;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //relaciona con la vista1 controles hasta el adaptador
        LayoutInflater infla = LayoutInflater.from(contexto);
       View v = infla.inflate(R.layout.vista3,parent,false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        //relacionar los datos con los controles del holder
        comentClass com = lista.get(position);

       holder.txtArrayComent.setText(com.getComentario());
       holder.txtnombres.setText(com.getNom()+" "+com.getApe());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        //variables para castear los controles con la lista
        TextView txtArrayComent,txtnombres;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            //relaciona los controles con la variable
            txtArrayComent= itemView.findViewById(R.id.txtArrayComent);
            txtnombres = itemView.findViewById(R.id.txtArrayNombre);



        }
    }
}
