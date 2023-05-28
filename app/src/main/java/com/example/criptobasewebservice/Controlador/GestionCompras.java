package com.example.criptobasewebservice.Controlador;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.criptobasewebservice.Modelo.Compra;
import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.conexionHTTP.Constantes;
import com.example.criptobasewebservice.conexionHTTP.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GestionCompras  implements Constantes {




    public static void actualizarSaldo(Usuario usuario, Double cantidad) {
        class RealizarCompraAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Crear el objeto JSON con los datos a enviar
                    JSONObject postData = new JSONObject();
                    postData.put("saldo", cantidad);
                    postData.put("usuario_id", usuario.getId());

                    // Establecer la conexión HTTP
                    URL url = new URL("http://" + ipConexion + "/Criptobase/actualizarSaldo.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Escribir los datos JSON en el cuerpo de la solicitud
                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(postData.toString());
                    wr.flush();
                    wr.close();

                    // Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }

        RealizarCompraAsyncTask task = new RealizarCompraAsyncTask();
        task.execute();
    }

    public static double retornarCantidadDeMoneda(Usuario usuario, String token) {

        String values = "token=" + token + "&usuario_id=" + usuario.getId();
        String resultado = HttpClient.GET_REQUEST(Constantes.URL_RETORNA_CANTIDAD_MONEDAS, values);


        double monedas = 0;
        // Parsear el resultado como un objeto JSON y obtener el campo "total_cantidad"
        try {
            JSONObject json = new JSONObject(resultado);
            monedas = json.getDouble("cantidad");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return monedas;

    }




}

