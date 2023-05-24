package com.example.criptobasewebservice.Modelo;


import java.io.Serializable;

public class Moneda implements Serializable {

    private int id;
    private String nombre, token, capitalizacion;

    private double precio;
    private int codigoUsuario;
    // private ImageIcon icono;

    public Moneda() {
    }

    public Moneda(int codigoUsuario, String nombre, String token, String capitalizacion) {

        this.nombre = nombre;
        this.token = token;
        this.capitalizacion = capitalizacion;
        this.codigoUsuario = codigoUsuario;
    }

    public Moneda(String nombre, String token, String capitalizacion,
                  double precio) {
        this.capitalizacion = capitalizacion;
        this.nombre = nombre;
        this.precio = precio;
        this.token = token;
    }

    public Moneda(String nombre, String token, String capitalizacion) {

        this.nombre = nombre;
        this.token = token;
        this.capitalizacion = capitalizacion;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCapitalizacion() {
        return capitalizacion;
    }

    public void setCapitalizacion(String capitalizacion) {
        this.capitalizacion = capitalizacion;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /*  public ImageIcon getIcono() {
          return icono;
      }

       public void setIconoFromBase64(String base64Icono) {
          try {
              byte[] bytesIcono = Base64.getDecoder().decode(base64Icono);
              this.icono = new ImageIcon(bytesIcono);
          } catch (IllegalArgumentException e) {
              // Handle error decoding Base64 string
          }
      }
  */
    @Override
    public String toString() {
        return "Moneda{" + "id=" + id + ", nombre=" + nombre + ", token=" + token + ", capitalizacion=" + capitalizacion + ", codigoUsuario=" + codigoUsuario + ", precio= " + precio + "}\n";
    }

}
