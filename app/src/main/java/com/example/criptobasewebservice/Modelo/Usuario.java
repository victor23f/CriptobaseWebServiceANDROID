package com.example.criptobasewebservice.Modelo;

import java.io.Serializable;

public class Usuario extends Persona implements Serializable {

    double saldo;

    public Usuario() {
        super();
    }

    public Usuario(int id, String usuario, String password, String nombre, String apellidos, String teléfono, String email) {
        super(id, usuario, password, nombre, apellidos, teléfono, email);

    }

    public Usuario(String usuario, String password, String nombre, String apellidos, String teléfono, String email) {
        super(usuario, password, nombre, apellidos, teléfono, email);
    }

    public Usuario(String usuario, String password, String nombre, String apellidos, String teléfono, String email, double saldo) {
        super(usuario, password, nombre, apellidos, teléfono, email);
        this.saldo = saldo;
    }

    public Usuario(int id) {
        super(id);
    }

    public Usuario(int id, String nombre, String password) {
        super(id, nombre, password);

    }

    public Usuario(String usuario, String password) {
        super(usuario, password);
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Usuario-> " + super.toString() + " saldo: " + saldo;
    }

}
