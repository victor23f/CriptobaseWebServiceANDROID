package com.example.criptobasewebservice.conexionHTTP;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpClient {

    /**
     * Enviar peticiones de Actualizacion
     *
     * @param url la url del servidor
     * @param values el valor que pedimos
     * @return retorna la consulta
     */
    public static String POST_REQUEST(String url, String values) {
        try {
            StringBuilder result = new StringBuilder();
            URL url2 = new URL(url);
            URLConnection conn = url2.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(values);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            wr.close();
            rd.close();
            return result.toString();

        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public static void POST_REQUEST_IMAGE(String url, File file, String nombreMascota, String codMascota) {
        String extension = obtenerExtension(file);
        try {
            URL url2 = new URL(url + "?filename=" + nombreMascota + "_" + codMascota + "." + extension);
            System.out.println(url2);
            URLConnection conn = url2.openConnection();
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
            long totalByte = fis.available();
            long byteTrasferred = 0;
            for (int j = 0; j < totalByte; j++) {
                os.write(fis.read());
                byteTrasferred = j + 1;
            }

            os.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));

            String s = null;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            in.close();
            fis.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String obtenerExtension(File file) {
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = file.getName().substring(i + 1);
        }
        return extension;
    }

    /**
     * Solicitar la ejecucion de consultas select
     *
     * @param url la url del servidor
     * @param values el valor que pedimos
     * @return retorna la consulta
     */
    public static String GET_REQUEST(String url, String values) {


        try {
            StringBuilder result = new StringBuilder();
            URL obj = new URL(url + "?" + values);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            in.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Object recogerObjetoJson(Class<?> clase, String json) {
        Gson gson = new Gson();
        Object objeto = gson.fromJson(json, clase);

        return objeto;
    }

    public static Object[] recogerArrayJson(Class<?> clase, String json) {
        Gson gson = new Gson();
        Type tipo = TypeToken.getArray(clase).getType();
        Object[] objetos = gson.fromJson(json, tipo);

        return objetos;
    }

    /**
     * Solicita la ejecucion de consultas select sin parametros
     *
     * @param url la url del servidor
     * @return retorna la consulta
     */
    public static String GET_REQUEST_SIN_PARAMETROS(String url) {
        try {
            StringBuilder result = new StringBuilder();
            String URL = url;
            java.net.URL obj = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result.append(inputLine);
            }
            return result.toString();

        } catch (MalformedURLException e) {
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }



}
