package com.evidenta.model;

import com.evidenta.interfaces.Exportable;

public class Executant extends Persoana implements Exportable {
    private String rol;

    public Executant(int id, String nume, String prenume, String email, String rol) {
        super(id, nume, prenume, email);
        this.rol = rol;
    }

    @Override
    public String getInfo() {
        return "Executant: " + prenume + " " + nume + " | Rol: " + rol;
    }

    @Override
    public String toCsv() {
        return id + "," + nume + "," + prenume + "," + email + "," + rol;
    }

    @Override
    public String toTxt() {
        return "ID: " + id + " | Nume: " + prenume + " " + nume +
               " | Email: " + email + " | Rol: " + rol;
    }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() { return prenume + " " + nume; }
}