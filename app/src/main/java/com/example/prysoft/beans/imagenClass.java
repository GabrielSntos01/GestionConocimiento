package com.example.prysoft.beans;

import java.io.Serializable;

public class imagenClass extends moduloClass implements Serializable {
     String idImagen;
     String nombre;

    public String getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(String idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
