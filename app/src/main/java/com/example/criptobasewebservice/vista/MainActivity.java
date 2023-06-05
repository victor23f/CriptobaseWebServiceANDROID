package com.example.criptobasewebservice.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.criptobasewebservice.Controlador.login;
import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.R;
import com.example.criptobasewebservice.conexionHTTP.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements Constantes{
    EditText username;
    EditText password;
    Button loginButton;
    TextView registerButton;
    Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


    }

    public void registrar(View vista){
        Intent ventanaRegistro = new Intent(MainActivity.this, Registro.class);
        startActivity(ventanaRegistro);
    }

    public void logear(View vista) {
        MainActivity mensaje = new MainActivity();
        class InsertarAsyncTask extends AsyncTask<String, Void, String> {

            private String insertarData(Usuario usuario) throws Exception {
                // Crear el objeto JSON con los datos a enviar
                JSONObject postData = new JSONObject();
                postData.put("usuario", usuario.getUsuario());
                postData.put("password", login.getMD5(usuario.getPassword()));

                // Crear la URL de la API
                URL url = new URL("http://" + ipConexion + "/Criptobase/loginClientes.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json"); // Establecer el tipo de contenido como JSON
                con.setDoOutput(true);

                // Escribir los datos JSON en el cuerpo de la solicitud
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(postData.toString());
                wr.flush();
                wr.close();

                // Leer la respuesta del archivo PHP
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String lineaEntrada;
                StringBuilder response = new StringBuilder();
                while ((lineaEntrada = in.readLine()) != null) {
                    response.append(lineaEntrada);
                }
                in.close();
                // Devolver la respuesta del archivo PHP
                return response.toString();

            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    return insertarData(new Usuario(username.getText().toString(), password.getText().toString()));
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(s);
                        if (jsonResponse.isNull("usuario")) {
                            mensaje.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Error en el logueo", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                             usuario = new Usuario(jsonResponse.getInt("id"),jsonResponse.getString("nombre"), jsonResponse.getString("password"));
                            // Haz algo con el objeto usuario
                            mensaje.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Logueo Correcto", Toast.LENGTH_SHORT).show();
                                    Intent ventanaMonedas = new Intent(MainActivity.this, OperacionesMonedas.class);
                                    ventanaMonedas.putExtra("usuario",usuario);
                                    startActivity(ventanaMonedas);

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mensaje.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error en el logueo", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        }

        InsertarAsyncTask task = new InsertarAsyncTask();
        task.execute();
    }


}