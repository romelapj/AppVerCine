package com.romelapj.appvercine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.romelapj.appvercine.R;
import com.romelapj.appvercine.activity.YoutubeActivity;
import com.romelapj.appvercine.model.ProximaPelicula;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by romelapj on 13/04/15.
 */
public class ProximaPeliculaAdapter extends RecyclerView.Adapter<ProximaPeliculaAdapter.PeliculaViewHolder> {
    public static List<ProximaPelicula> items;
    public static Context context;
    public static PeliculaViewHolder viewHolder;
    public AdapterViewCompat.OnItemClickListener mItemClickListener;
    private int numeroItem;

    public static class PeliculaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imagen;
        public TextView nombre;
        public TextView fecha;
        public ImageView compartir;

        public PeliculaViewHolder(View v) {
            super(v);
            nombre= (TextView) v.findViewById(R.id.nombre);
            imagen= (ImageView) v.findViewById(R.id.imagen);
            fecha= (TextView) v.findViewById(R.id.fecha);
            compartir= (ImageView) v.findViewById(R.id.compartir);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context, YoutubeActivity.class);
            intent.putExtra("pos",getPosition());
            context.startActivity(intent);
        }
    }

    public ProximaPeliculaAdapter(List<ProximaPelicula> items, Context context) {
        this.context=context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public PeliculaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pelicula_card, viewGroup, false);
        return new PeliculaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PeliculaViewHolder viewHolder, int i) {
        String imagen=items.get(i).getImagen();
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.fecha.setText(items.get(i).getFecha());
        viewHolder.compartir.setTag(i);
        Picasso.with(context)
                .load(items.get(i).getImagen())
                .placeholder(R.drawable.img_pelicula)
                .error(R.drawable.img_pelicula)
                .into(viewHolder.imagen);
        viewHolder.compartir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                numeroItem=Integer.parseInt(v.getTag().toString());
                Uri imageUri=getLocalBitmapUri(viewHolder.imagen);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, items.get(numeroItem).getNombre()+" "+items.get(numeroItem).getFecha());
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, "App Ver Cine"));
            }

        });

    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}
