package com.example.criptobasewebservice.Modelo;

import java.io.Serializable;

/**
 *
 * @author victo
 */
public class Admin extends Persona implements Serializable {

    public Admin(int id, String usuario, String password, String nombre, String apellidos, String teléfono, String email) {
        super(id, usuario, password, nombre, apellidos, teléfono, email);
    }

    public Admin(int id) {
        super(id);
    }

    public Admin(int id, String nombre, String password) {
        super(id, nombre, password);
    }

}
