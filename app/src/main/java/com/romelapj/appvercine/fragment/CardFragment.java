package com.romelapj.appvercine.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.romelapj.appvercine.R;
import com.romelapj.appvercine.adapter.ProximaPeliculaAdapter;
import com.romelapj.appvercine.model.ProximaPelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends android.support.v4.app.Fragment {

    private static final String ARG_POSITION = "position";
    private ArrayAdapter<String> mForecastAdapter;
    private static final int CARTELERA_PELICULA=0;
    private static final int PROXIMA_PELICULA=1;


    private int position;
    @InjectView(R.id.reciclador)
    RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapterCartelera;
    private RecyclerView.LayoutManager lManager;
    private List<ProximaPelicula> items = new ArrayList<>();

    public static CardFragment newInstance(int position) {
        CardFragment f = new CardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        setHasOptionsMenu(true);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.lista_card,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);



            // Obtener el Recycler
            recycler.setHasFixedSize(true);

            // Usar un administrador para LinearLayout
            lManager = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(lManager);
            ConexionesPeliculasTask weatherTask = new ConexionesPeliculasTask();
            weatherTask.execute();
            // Crear un nuevo adaptador
            adapter = new ProximaPeliculaAdapter(items, getActivity());
            recycler.setAdapter(adapter);



        return rootView;
    }
    public class ConexionesPeliculasTask extends AsyncTask<Void, Void, List<ProximaPelicula>> {

        private final String LOG_TAG = ConexionesPeliculasTask.class.getSimpleName();
        private List<ProximaPelicula> getDataFromJson(String peliculasJsonStr)
                throws JSONException {
            JSONObject peliculasJson = new JSONObject(peliculasJsonStr);
            JSONObject resultadosJson=peliculasJson.getJSONObject("results");
            JSONArray collectionArray = resultadosJson.getJSONArray("collection1");
            List<ProximaPelicula> resultadoProximaPelicula =new ArrayList<>();

            for(int i = 0; i < collectionArray.length(); i++) {
                String titulo;
                String descripcion;
                String imagenUrl;
                String trailer;
                JSONObject peliculaEstreno = collectionArray.getJSONObject(i);
                titulo=peliculaEstreno.getString("Titulo");
                descripcion=peliculaEstreno.getString("fecha");
                imagenUrl=peliculaEstreno.getJSONObject("imagen").getString("src");
                trailer=peliculaEstreno.getJSONObject("trailer").getString("href");
                resultadoProximaPelicula.add(new ProximaPelicula(imagenUrl,titulo,descripcion,trailer));

            }


            return resultadoProximaPelicula;

        }
        @Override
        protected List<ProximaPelicula> doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            try {
                URL url = new URL("https://www.kimonolabs.com/api/4xk2qcwg?apikey=MbWe0XWMyyVyDgXUqYaSgBwvTfi8lzWo");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<ProximaPelicula> result) {
            if (result != null) {
                adapter=new ProximaPeliculaAdapter(result,getActivity());
                recycler.setAdapter(adapter);
            }
        }
    }

}
