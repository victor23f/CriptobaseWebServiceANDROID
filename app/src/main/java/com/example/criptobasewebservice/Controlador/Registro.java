package com.example.criptobasewebservice.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Registro extends AppCompatActivity {

    final String ipConexion="192.168.1.129";
    EditText username,password,nombre,apellidos,telefono,email;

    TextView registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);

        registerButton = findViewById(R.id.loginButton);
    }
    public void insertarPersona(View vista) {
        Registro mensaje = new Registro();
        class insertarAsyncTask extends AsyncTask<String, Void, String> {

            private String insertarData(Usuario usuario) throws Exception {
                // Crear el objeto JSON con los datos a enviar
                JSONObject postData = new JSONObject();
                postData.put("usuario", usuario.getUsuario());
                postData.put("password", usuario.getPassword());
                postData.put("nombre", usuario.getNombre());
                postData.put("apellidos", usuario.getApellidos());
                postData.put("telefono", usuario.getTelefono());
                postData.put("email", usuario.getEmail());

                // Crear la URL de la API
                URL url = new URL("http://" + ipConexion + "/Criptobase/registrarUsuario.php");
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
                // String nombre = strings[0];
                // String definicion = strings[1];
                try {
                    return insertarData(new Usuario(username.getText().toString(), password.getText().toString(), nombre.getText().toString(), apellidos.getText().toString(),telefono.getText().toString(), email.getText().toString()));
                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    //si se ha insertado, mostramos el resultado
                    mensaje.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Registro.this, "Insertada correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mensaje.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Registro.this, "Error en la inserción", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
        insertarAsyncTask task = new insertarAsyncTask();
        task.execute();

    }
}