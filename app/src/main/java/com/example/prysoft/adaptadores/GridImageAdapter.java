package com.example.prysoft.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.prysoft.R;
import com.example.prysoft.beans.imagenClass;

import java.util.List;

public class GridImageAdapter extends BaseAdapter {

    String url = "https://gestionconocimientotallerinve.000webhostapp.com/pryGestion/imagenes/";
   // String url="http://10.0.2.2/pryGestion/imagenes/";
    List<imagenClass> list;
    Context contexto;
    LayoutInflater layoutInflater;

    public GridImageAdapter( Context contexto, List<imagenClass> list) {
        this.list = list;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        imagenClass i = list.get(position);

        if(view==null){

            layoutInflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.from(contexto).inflate(R.layout.vista2,null);

        }


        ImageView img = view.findViewById(R.id.imgAP);

        Glide.with(contexto)
                .load(url+i.getNombre()+".png")
                .placeholder(R.drawable.carga)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(300,300)
                .into(img);

        return view;

    }
}
