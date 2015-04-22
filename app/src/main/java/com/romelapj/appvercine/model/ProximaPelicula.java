package com.romelapj.appvercine.model;

/**
 * Created by romelapj on 13/04/15.
 */
public class ProximaPelicula {
    private String imagen;
    private String nombre;
    private String fecha;
    private String codeTrailer;

    public ProximaPelicula(String imagen, String nombre, String fecha, String trailer) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.fecha = fecha;
        this.codeTrailer=obtenerCodeTrailer(trailer);
    }
    public String obtenerCodeTrailer(String trailer){
        String[] tempTrailer=trailer.split("\\?");
        tempTrailer=tempTrailer[0].split("/");
        return tempTrailer[tempTrailer.length-1];
    }
    public String getTrailer(){return codeTrailer;}

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getImagen() {
        return imagen;
    }
}
