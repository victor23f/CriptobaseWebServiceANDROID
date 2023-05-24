package com.example.criptobasewebservice.Controlador;

import static com.example.criptobasewebservice.conexionHTTP.HttpClient.POST_REQUEST;

import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.conexionHTTP.Constantes;
import com.example.criptobasewebservice.conexionHTTP.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class login {

    public static String registrarUsuario(Usuario usuario) {
        JSONObject json_usuario = new JSONObject();
        try {
        json_usuario.put("usuario", usuario.getUsuario());
        json_usuario.put("password", getMD5(usuario.getPassword()));
        json_usuario.put("nombre", usuario.getNombre());
        json_usuario.put("apellidos", usuario.getApellidos());
        json_usuario.put("telefono", usuario.getTelefono());
        json_usuario.put("email", usuario.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String values = json_usuario.toString();
        String response = POST_REQUEST(Constantes.URL_INGRESAR_USUARIO, values);

        if (response == null) {
            return "Error al enviar la solicitud POST";
        } else {
            return response;
        }

    }

    public static String modificarUsuario(Usuario usuario, String[] valoresNuevos) {
        JSONObject json = new JSONObject();
        try{
        json.put("id", usuario.getId());
        json.put("nombre", valoresNuevos[0]);
        json.put("usuario", valoresNuevos[1]);
        json.put("email", valoresNuevos[2]);
        json.put("telefono", valoresNuevos[3]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String valores = json.toString();
        String response = POST_REQUEST(Constantes.URL_MODIFICAR_USUARIO, valores);
        return response;
    }

    public static String eliminarUsuario(Usuario usuario) {
        JSONObject json = new JSONObject();
        try{
        json.put("id", usuario.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String valores = json.toString();
        String response = POST_REQUEST(Constantes.URL_ELIMINAR_USUARIO, valores);
        return response;
    }

    public static ArrayList<Usuario> devolverUsuariosBD() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String response = HttpClient.GET_REQUEST_SIN_PARAMETROS(Constantes.URL_LISTADOS_USUARIOS);
        for (Object usuario : HttpClient.recogerArrayJson(Usuario.class, response)) {

            usuarios.add((Usuario) usuario);

        }

        return usuarios;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}

