package com.example.criptobasewebservice.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.criptobasewebservice.Controlador.GestionCompras;
import com.example.criptobasewebservice.Controlador.MonedaAdapter;
import com.example.criptobasewebservice.Modelo.Compra;
import com.example.criptobasewebservice.Modelo.Moneda;
import com.example.criptobasewebservice.Modelo.Usuario;
import com.example.criptobasewebservice.R;
import com.example.criptobasewebservice.conexionHTTP.Constantes;

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

public class OperacionesMonedas extends AppCompatActivity implements Constantes {
    static double saldoUsuario, cantidadMonedaUsuario;
    private List<Moneda> listaMonedas;
    private Usuario usuario;
    private static TextView usuarioText, saldoText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
        setContentView(R.layout.activity_operaciones_monedas);
        usuarioText = findViewById(R.id.textViewUsuario);
        saldoText = findViewById(R.id.textViewSaldo);
        Bundle datos = getIntent().getExtras();
        // Verificar si existen extras y si contiene la clave "usuario"
        if (datos != null && datos.containsKey("usuario")) {
            // Obtener el objeto Usuario de los extras
            usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            retornaSaldo(usuario);
            // Realizar las acciones necesarias con el objeto Usuario
            // ...
        }
        usuarioText.setText(usuario.getUsuario());

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
        actualizaCantidadDeMoneda(usuario,listaMonedas.get(posicion).getToken());
        builder.setTitle("COMPRA/VENTA: " + listaMonedas.get(posicion).getToken() + "  " + listaMonedas.get(posicion).getPrecio() + "$")
                .setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cantidadDolares.getText().toString().equals("")) {
                        } else {
                            double cantidad = Double.parseDouble(cantidadDolares.getText().toString());
                            comprarMoneda(new Compra(listaMonedas.get(posicion).getToken(), usuario.getId(), cantidad));
                            double cantidadMonedas = cantidad / listaMonedas.get(posicion).getPrecio();
                            // Realizar las acciones necesarias con la cantidad introducida
                            if (cantidad != 0 && (cantidad < saldoUsuario || cantidad == saldoUsuario)) {
                                GestionCompras.actualizarSaldo(usuario, (saldoUsuario - cantidad));
                                Toast.makeText(OperacionesMonedas.this, "Compra realizada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OperacionesMonedas.this, "No se ha podido realizar la compra, revisa tu saldo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                })
                .setNegativeButton("Vender", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (cantidadDolares.getText().toString().equals("")) {
                        } else {
                            double cantidad = Double.parseDouble(cantidadDolares.getText().toString());

                            double cantidadMonedas = cantidad / listaMonedas.get(posicion).getPrecio();
                            // Realizar las acciones necesarias con la cantidad introducida
                            if (cantidadMonedaUsuario >cantidadMonedas) {
                                GestionCompras.actualizarSaldo(usuario, (saldoUsuario + (cantidadMonedas * listaMonedas.get(posicion).getPrecio())));
                                comprarMoneda(new Compra(listaMonedas.get(posicion).getToken(), usuario.getId(), -cantidad));
                                Toast.makeText(OperacionesMonedas.this, "Venta realizada correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OperacionesMonedas.this, "No se ha podido realizar la venta, monedas insuficientes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        ;
        actualizaCantidadDeMoneda(usuario,listaMonedas.get(posicion).getToken());
        return builder;
    }

    public void listaDeMonedas(ArrayList<Moneda> monedasList) {
        MonedaAdapter adaptador = new MonedaAdapter(this, monedasList);

        ListView listaComp = findViewById(R.id.listaMonedas);
        listaComp.setAdapter(adaptador);

        listaComp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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
                            retornaSaldo(usuario);
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

    public static void retornaSaldo(Usuario usuario) {

        class RealizarCompraAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Establecer la conexión HTTP
                    URL url = new URL("http://" + ipConexion + "/Criptobase/retornaSaldo.php?id=" + usuario.getId());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

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
            protected void onPostExecute(String s) {
                // Analizar la respuesta JSON y obtener el número
                if (s != null) {
                    try {
                        JSONObject jsonResponse = new JSONObject(s);
                        if (jsonResponse.has("saldo")) {
                            saldoUsuario = jsonResponse.getDouble("saldo");
                            saldoText.setText("SALDO:    " + saldoUsuario + "$");

                            // Haz algo con el saldo obtenido
                        } else if (jsonResponse.has("error")) {
                            String error = jsonResponse.getString("error");
                            // Haz algo con el mensaje de error
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Manejar el caso en que no se reciba una respuesta válida
                }
            }
        }

        RealizarCompraAsyncTask task = new RealizarCompraAsyncTask();
        task.execute();


    }
    public static void actualizaCantidadDeMoneda(Usuario usuario, String token) {
        class RetornarCantidadMonedaAsyncTask extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Crear la cadena de valores para la solicitud GET
                    URL url = new URL("http://" + ipConexion + "/Criptobase/retornaCantidadMoneda.php?usuario_id=" + usuario.getId() + "&token="+token);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Type", "application/json");

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
                try {
                    JSONObject json = new JSONObject(result);
                    cantidadMonedaUsuario = json.getDouble("cantidad");
                    System.out.println(cantidadMonedaUsuario);
                    System.out.println(json.getDouble("cantidad"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RetornarCantidadMonedaAsyncTask task = new RetornarCantidadMonedaAsyncTask();
        task.execute();
    }

}


