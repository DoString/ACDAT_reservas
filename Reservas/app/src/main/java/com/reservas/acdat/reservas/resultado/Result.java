package com.reservas.acdat.reservas.resultado;

import com.reservas.acdat.reservas.PeriodoOcupado;
import com.reservas.acdat.reservas.Reserva;
import com.reservas.acdat.reservas.usuarios.PeriodoLibre;
import com.reservas.acdat.reservas.usuarios.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by juan on 23/02/2016.
 */
public class Result implements Serializable {
    boolean code;
    int status;
    String message;
    Usuario usuario;
    ArrayList<Reserva> reservas;
    ArrayList<Usuario> users;

    public ArrayList<Usuario> getUsers() {
        return users;
    }

    //-- libres
    ArrayList<PeriodoLibre> libres;
    // -- ocupados
    ArrayList<PeriodoOcupado>  ocupados;

    public ArrayList<PeriodoOcupado> getOcupados() {
        return ocupados;
    }

    public ArrayList<PeriodoLibre> getLibres() {
        return libres;
    }

    public int getStatus() {
        return status;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public boolean getCode() {
        return code;
    }

    public boolean isCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Usuario getUsuario() {
        return usuario;
    }

}
