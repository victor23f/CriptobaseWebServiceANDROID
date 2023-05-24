package com.example.criptobasewebservice.Modelo;

import java.io.Serializable;

public class Persona implements Serializable {

    private int id;
    private String usuario, password, nombre, apellidos, telefono, email;

    public Persona() {

    }

    public Persona(int codigo, String usuario, String password, String nombre, String apellidos, String teléfono, String email) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = teléfono;
        this.email = email;
        this.id = codigo;
    }

    public Persona(String usuario, String password, String nombre, String apellidos, String teléfono, String email) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = teléfono;
        this.email = email;
    }

    public Persona(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public Persona(int codigo) {
        this.id = codigo;
    }

    public Persona(int codigo, String usuario, String password) {
        this.id = codigo;
        this.usuario = usuario;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "codigo= " + id + ", usuario= " + usuario + ", password= " + password + ", nombre= " + nombre + ", apellidos= " + apellidos + ", telefono= " + telefono + ", email= " + email + "\n";
    }

}

