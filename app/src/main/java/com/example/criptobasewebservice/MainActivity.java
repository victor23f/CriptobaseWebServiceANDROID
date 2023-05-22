package com.example.criptobasewebservice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // final String ipConexion = "192.168.1.85";
    final String ipConexion="26.0.0.1";
    EditText username;
    EditText password;
    Button loginButton;
    TextView registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.signupText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
                    Toast.makeText(MainActivity.this, "Login correcto!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Login fallido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void insertUsuario() {
        MainActivity mensaje = new MainActivity();
        class insertarAsyncTask extends AsyncTask<String, Void, String> {

            private String insertarData(String nombre, String password) throws Exception {
                //enviamos solicitud HTTP POST al archivo PHP
                URL url = new URL("http://" + ipConexion + "/Criptobase/consultasClientes.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                //agregar los datos a enviar en la solicitud HTTP POST

                String urlParameters = "?usuario=" + nombre + "&password=" + password;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                //Leemos la respuesta del archivo PHP
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String lineaEntrada;
                StringBuffer response = new StringBuffer();
                while ((lineaEntrada = in.readLine()) != null) {
                    response.append(lineaEntrada);
                }
                in.close();

                //devolvemos respuesta al archivo PHP

                return response.toString();
            }


            @Override
            protected String doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    //si se ha insertado, mostramos el resultado
                    mensaje.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mensaje.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error en el registro del usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
        insertarAsyncTask task = new insertarAsyncTask();
        task.execute();

    }
}