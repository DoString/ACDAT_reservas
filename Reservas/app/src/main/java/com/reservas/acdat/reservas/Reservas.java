package com.reservas.acdat.reservas;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.reservas.acdat.reservas.usuarios.Usuario;

public class Reservas extends Application {
    public static final String URL_LOGIN = "https://portadaalta.club/juan/slimrest/login";
    public static final String URL_RESERVAS_MIAS = "https://portadaalta.club/juan/slimrest/reservas";
    public static final String URL_LIBRES = "https://portadaalta.club/juan/slimrest/reservas/libres";
    public static final String URL_BORRAR = "https://portadaalta.club/juan/slimrest/reservas/borrar";
    public static final String URL_OCUPADOS_PERIODOS = "https://portadaalta.club/juan/slimrest/reservas/ocupados";
    public static final String URL_USERS = "https://portadaalta.club/juan/slimrest/users";
    public static final String URL_EMAIL = "https://portadaalta.club/juan/slimrest/email";

    /*public static final String URL_LOGIN = "http://192.168.1.10/slimrest/login";
    public static final String URL_RESERVAS_MIAS = "http://192.168.1.10/slimrest/reservas";
    public static final String URL_LIBRES = "http://192.168.1.10/slimrest/reservas/libres";
    public static final String URL_BORRAR = "http://192.168.1.10/slimrest/reservas/borrar";
    public static final String URL_OCUPADOS = "http://192.168.1.10/slimrest/reservas/aula";
    public static final String URL_OCUPADOS_PERIODOS = "http://192.168.1.10/slimrest/reservas/ocupados";
    public static final String URL_USERS = "http://192.168.1.10/slimrest/users";
    public static final String URL_EMAIL = "http://192.168.1.10/slimrest/email";*/
    private AsyncHttpClient client = new AsyncHttpClient();
    private Usuario usuario = null;
    private Reserva seleccionada = null;
    private int accion = -1;

    public static final int UPDATE = 1;
    public static final int ADD = 0;
    public static final int LIBRES = 2;
    public static final int OCUPADOS = 3;

    private int aula;
    private String fechaIn, fechaFin;

    public int getAula() {
        return aula;
    }

    public void setAula(int aula) {
        this.aula = aula;
    }

    public String getFechaIn() {
        return fechaIn;
    }

    public void setFechaIn(String fechaIn) {
        this.fechaIn = fechaIn;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public Reserva getSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(Reserva seleccionada) {
        this.seleccionada = seleccionada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public AsyncHttpClient getClient() {
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        return client;
    }
}
