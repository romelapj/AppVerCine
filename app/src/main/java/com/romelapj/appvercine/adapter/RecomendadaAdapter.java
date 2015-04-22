package com.romelapj.appvercine.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.romelapj.appvercine.R;
import com.romelapj.appvercine.model.ProximaPelicula;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by romelapj on 17/04/15.
 */
public class RecomendadaAdapter extends ArrayAdapter {
    public Context context;
    public List<ProximaPelicula> datos;
    @InjectView(R.id.ivRecomendado)
    ImageView ivRecomendado;
    @InjectView(R.id.tvRecomendado)
    TextView tvRecomendado;

    public RecomendadaAdapter(Context context, List datos) {
        super(context, R.layout.item_recomendado,datos);
        this.context = context;
        this.datos = datos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_recomendado, null);
        ButterKnife.inject(this, item);
        Picasso.with(context)
                .load(datos.get(position).getImagen())
                .placeholder(R.drawable.img_pelicula)
                .error(R.drawable.img_pelicula)
                .into(ivRecomendado);
        tvRecomendado.setText(datos.get(position).getNombre());


        return item;
    }

}
