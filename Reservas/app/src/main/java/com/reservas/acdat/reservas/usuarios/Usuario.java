package com.reservas.acdat.reservas.usuarios;

public class Usuario {
    private String nombre, apellidos, email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private int nivel, id;

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return nombre + ", " + apellidos;
    }
}
