package com.example.alertasww3.model;

public class Alerta {
    private int id;
    private String titulo;
    private String descripcion;
    private long fechahora;
    private String nivel;

    public static final Alerta ALERTA_NULA = new Alerta(0, "Sin Alerta", "No hay información disponible.",  0,  "");

    public Alerta(int id, String titulo, String descripcion, long fechahora, String nivel){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechahora = fechahora;
        this.nivel = nivel;
    }

    public int getId(){
        return this.id;
    }
    public String getTitulo(){
        return this.titulo;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    public long getFechahora(){
        return this.fechahora;
    }
    public String getNivel(){
        return this.nivel;
    }

    public void setFechahora(long fechahora){
        this.fechahora = fechahora;
    }
}
