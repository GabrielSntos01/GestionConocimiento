package com.example.prysoft.beans;

import java.io.Serializable;

public class comentClass extends moduloClass implements Serializable {
    String comentario;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
