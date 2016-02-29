package com.reservas.acdat.reservas;

import com.reservas.acdat.reservas.usuarios.PeriodoLibre;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by juan on 24/02/2016.
 */
public class Reserva implements Serializable {
    int id;
    int horaInt;
    int aula;
    int profesorInt;
    String profesor, nombre, periodo, dia, hora;

    public int getAula() {
        return aula;
    }

    public void setAula(int aula) {
        this.aula = aula;
    }


    public void setProfesorInt(int profesorInt) {
        this.profesorInt = profesorInt;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    //-- isertar / actualizar
    String fechaIn, fechaFin;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setHoraInt(int horaInt) {
        this.horaInt = horaInt;
    }

    public int getHoraInt() {
        return horaInt;
    }

    public int getId() {
        return id;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    @Override
    public String toString() {
        return profesor + "\n" +
                nombre + "\n" +
                periodo + "\n" +
                dia + "\n" +
                hora;
    }
}
