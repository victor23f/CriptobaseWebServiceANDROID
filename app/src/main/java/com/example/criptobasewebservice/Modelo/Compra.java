package com.example.criptobasewebservice.Modelo;

public class Compra {

    private String token;
    private int usuario_id;
    private double cantidad;

    public Compra() {
    }

    public Compra(String token, int usuario_id, double cantidad) {
        this.token = token;
        this.usuario_id = usuario_id;
        this.cantidad = cantidad;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Compra{" + "token=" + token + ", usuario_id=" + usuario_id + ", cantidad=" + cantidad + '}';
    }

}
