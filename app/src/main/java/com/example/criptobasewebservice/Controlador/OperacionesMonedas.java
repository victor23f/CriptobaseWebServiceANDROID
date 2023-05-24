package com.example.criptobasewebservice.Controlador;

import static com.example.criptobasewebservice.conexionHTTP.HttpClient.POST_REQUEST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.criptobasewebservice.Modelo.Compra;
import com.example.criptobasewebservice.Modelo.Moneda;
import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.R;
import com.example.criptobasewebservice.conexionHTTP.Constantes;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OperacionesMonedas extends AppCompatActivity {

    private ListView listView;
    private List<Moneda> listaMonedas;
    final String ipConexion = "192.168.1.129";
    private Usuario usuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        setContentView(R.layout.activity_operaciones_monedas);

        Bundle datos = getIntent().getExtras();
        // Verificar si existen extras y si contiene la clave "usuario"
        if (datos != null && datos.containsKey("usuario")) {
            // Obtener el objeto Usuario de los extras
            usuario = (Usuario)getIntent().getSerializableExtra("usuario");

            // Realizar las acciones necesarias con el objeto Usuario
            // ...
        }
        System.out.println(usuario);
        listaMonedas = new ArrayList<>();
        recogerMonedas();
    }
    public AlertDialog.Builder mostrarCompraVentaMoneda(int posicion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText cantidadDolares = new EditText(this);


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Configurar el EditText para aceptar solo números (incluyendo decimales)
        cantidadDolares.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        cantidadDolares.setHint("Introduce la cantidad");

        layout.addView(cantidadDolares);
        builder.setView(layout);

        builder.setTitle("COMPRA/VENTA: " + listaMonedas.get(posicion).getToken() + "  " + listaMonedas.get(posicion).getPrecio()+"$" )
                .setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Double cantidad = Double.parseDouble(cantidadDolares.getText().toString());
                        comprarMoneda(new Compra(listaMonedas.get(posicion).getToken(), usuario.getId(), cantidad));
                        // Realizar las acciones necesarias con la cantidad introducida
                        // ...
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });;

        return builder;
    }

    public void listaDeMonedas(ArrayList<Moneda> monedasList) {
        MonedaAdapter adaptador = new MonedaAdapter(this, monedasList);

        ListView listaComp = findViewById(R.id.listaMonedas);
        listaComp.setAdapter(adaptador);

        listaComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(OperacionesMonedas.this, listaMonedas.get(position).toString(), Toast.LENGTH_SHORT).show();
                mostrarCompraVentaMoneda(position).show();
            }
        });
    }

    public void recogerMonedas() {
        class ObtenerMonedasAsyncTask extends AsyncTask<Void, Void, List<Moneda>> {

            @Override
            protected List<Moneda> doInBackground(Void... voids) {
                try {
                    // Establecer la conexión HTTP
                    URL url = new URL("http://" + ipConexion + "/Criptobase/obtenerMonedas.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // Procesar la respuesta JSON
                    JSONArray jsonArray = new JSONArray(response.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String token = jsonObject.getString("token");
                        String nombre = jsonObject.getString("nombre");
                        String capitalizacion = jsonObject.getString("capitalizacion");
                        double precio = jsonObject.getDouble("precio");

                        Moneda moneda = new Moneda(nombre, token, capitalizacion, precio);
                        listaMonedas.add(moneda);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return listaMonedas;
            }

            @Override
            protected void onPostExecute(List<Moneda> listaMonedas) {
                if (listaMonedas != null) {
                    // Cargar el ListView con la lista de monedas
                    listaDeMonedas((ArrayList<Moneda>) listaMonedas);
                } else {
                    // Error al obtener la lista de monedas
                    Toast.makeText(OperacionesMonedas.this, "Error al obtener las monedas", Toast.LENGTH_SHORT).show();
                }
            }
        }

        ObtenerMonedasAsyncTask task = new ObtenerMonedasAsyncTask();
        task.execute();
    }
    public void comprarMoneda(Compra compra) {
        class RealizarCompraAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Crear el objeto JSON con los datos a enviar
                    JSONObject postData = new JSONObject();
                    postData.put("cantidad", compra.getCantidad());
                    postData.put("token", compra.getToken());
                    postData.put("usuario_id", compra.getUsuario_id());

                    // Establecer la conexión HTTP
                    URL url = new URL("http://" + ipConexion + "/Criptobase/realizarCompra.php");
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
                if (result != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(result);
                        if (jsonResponse.has("message")) {
                            // La compra se realizó correctamente
                            Toast.makeText(OperacionesMonedas.this, "Compra realizada correctamente", Toast.LENGTH_SHORT).show();
                        } else if (jsonResponse.has("error")) {
                            // Error al realizar la compra
                            String errorMessage = jsonResponse.getString("error");
                            Toast.makeText(OperacionesMonedas.this, "Error al realizar la compra: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Error de conexión o respuesta nula
                    Toast.makeText(OperacionesMonedas.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            }
        }

        RealizarCompraAsyncTask task = new RealizarCompraAsyncTask();
        task.execute();
    }

}
