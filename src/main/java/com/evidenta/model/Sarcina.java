package com.evidenta.model;

import com.evidenta.interfaces.Exportable;

public class Sarcina implements Exportable {
    private int id;
    private String titlu;
    private String descriere;
    private int prioritate;
    private StatusSarcina status;
    private int idProiect;
    private int idExecutant;
    private String numeProiect;
    private String numeExecutant;

    public Sarcina(int id, String titlu, String descriere, int prioritate,
                   StatusSarcina status, int idProiect, int idExecutant,
                   String numeProiect, String numeExecutant) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.prioritate = prioritate;
        this.status = status;
        this.idProiect = idProiect;
        this.idExecutant = idExecutant;
        this.numeProiect = numeProiect;
        this.numeExecutant = numeExecutant;
    }

    @Override
    public String toCsv() {
        return id + "," + titlu + "," + status + "," +
               prioritate + "," + numeProiect + "," + numeExecutant;
    }

    @Override
    public String toTxt() {
        return "ID: " + id + " | Titlu: " + titlu + " | Status: " + status +
               " | Prioritate: " + prioritate + " | Proiect: " + numeProiect +
               " | Executant: " + numeExecutant;
    }

    public int getId() { return id; }
    public String getTitlu() { return titlu; }
    public String getDescriere() { return descriere; }
    public int getPrioritate() { return prioritate; }
    public StatusSarcina getStatus() { return status; }
    public int getIdProiect() { return idProiect; }
    public int getIdExecutant() { return idExecutant; }
    public String getNumeProiect() { return numeProiect; }
    public String getNumeExecutant() { return numeExecutant; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public void setDescriere(String descriere) { this.descriere = descriere; }
    public void setPrioritate(int prioritate) { this.prioritate = prioritate; }
    public void setStatus(StatusSarcina status) { this.status = status; }
    public void setIdProiect(int idProiect) { this.idProiect = idProiect; }
    public void setIdExecutant(int idExecutant) { this.idExecutant = idExecutant; }

    @Override
    public String toString() { return titlu; }
}