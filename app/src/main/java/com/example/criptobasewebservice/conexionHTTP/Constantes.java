package com.example.criptobasewebservice.conexionHTTP;

public interface Constantes {
    final String VALIDARPASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\d\\s])[^\\s]{5,}$";
    final String VALIDAR_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    final String VALIDAR_TELEFONO_ESPAÑA = "(\\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}";
    public static final String SERVERUSUARIO = "http://192.168.1.129/Criptobase/";
    final static String ipConexion = "192.168.1.129";
    public static final String URL_LOGUEO_CLIENTE = SERVERUSUARIO + "loginClientes.php";
    public static final String URL_LOGUEO_ADMIN = SERVERUSUARIO + "loginAdmin.php";

    public static final String URL_LISTADOS_MONEDAS_DE_USUARIO = SERVERUSUARIO + "obtenerMonedasFavoritas.php";
    public static final String URL_LISTADOS_MONEDAS = SERVERUSUARIO + "obtenerMonedas.php";
    public static final String URL_LISTADOS_NOTAS_DE_USUARIO = SERVERUSUARIO + "obtenerNotas.php";

    public static final String URL_LISTADOS_USUARIOS = SERVERUSUARIO + "consultasClientes.php";
    public static final String URL_INGRESAR_MONEDA = SERVERUSUARIO + "ingresarMoneda.php";
    public static final String URL_INGRESAR_USUARIO = SERVERUSUARIO + "registrarUsuario.php";
    public static final String URL_INGRESAR_NOTA = SERVERUSUARIO + "ingresarNota.php";

    public static final String URL_INGRESAR_MONEDA_FAVORITA_USUARIO = SERVERUSUARIO + "aniadirMonedaFavoritaUsuario.php";
    public static final String URL_ELIMINAR_MONEDA_FAVORITA_USUARIO = SERVERUSUARIO + "eliminarMonedaFavoritaUsuario.php";

    public static final String URL_INGRESAR_COMPRA = SERVERUSUARIO + "realizarCompra.php";

    public static final String URL_ACTUALIZA_SALDO = SERVERUSUARIO + "actualizarSaldo.php";

    public static final String URL_RETORNA_SALDO = SERVERUSUARIO + "retornaSaldo.php";
    public static final String URL_RETORNA_CANTIDAD_MONEDAS = SERVERUSUARIO + "retornaCantidadMoneda.php";

    public static final String URL_ELIMINAR_USUARIO = SERVERUSUARIO + "eliminarUsuario.php";
    public static final String URL_MODIFICAR_USUARIO = SERVERUSUARIO + "modificarUsuario.php";
    public static final String CR_OK_INSERT = "OK_INSERT";
    public static final String CR_OK_DELETE = "OK_DELETE";
    public static final String CR_ERROR = "CR_ERROR";
}
