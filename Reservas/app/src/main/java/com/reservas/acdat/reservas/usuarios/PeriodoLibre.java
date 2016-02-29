package com.reservas.acdat.reservas.usuarios;

import java.io.Serializable;
import java.util.ArrayList;


public class PeriodoLibre implements Serializable {
    String fecha, dia;
    ArrayList<String> horas;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public ArrayList<String> getHoras() {
        return horas;
    }

    public void setHoras(ArrayList<String> horas) {
        this.horas = horas;
    }

    private String printHoras() {
        String horas1 = "";
        for (String h : horas) {
            horas1 += h + "\n";
        }
        return horas1;
    }

    @Override
    public String toString() {
        return dia + " " +  fecha + "\n" +
                "Horas Libres:" + "\n" +
                printHoras();
    }
}
