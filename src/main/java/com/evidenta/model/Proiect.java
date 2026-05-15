package com.evidenta.model;

import com.evidenta.interfaces.Exportable;

public class Proiect implements Exportable {
    private int id;
    private String titlu;
    private String descriere;
    private String dataStart;
    private String dataEnd;
    private String status;

    public Proiect(int id, String titlu, String descriere,
                   String dataStart, String dataEnd, String status) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.dataStart = dataStart;
        this.dataEnd = dataEnd;
        this.status = status;
    }

    @Override
    public String toCsv() {
        return id + "," + titlu + "," + descriere + "," +
               dataStart + "," + dataEnd + "," + status;
    }

    @Override
    public String toTxt() {
        return "ID: " + id + " | Titlu: " + titlu + " | Status: " + status +
               " | Start: " + dataStart + " | End: " + (dataEnd != null ? dataEnd : "-");
    }

    public int getId() { return id; }
    public String getTitlu() { return titlu; }
    public String getDescriere() { return descriere; }
    public String getDataStart() { return dataStart; }
    public String getDataEnd() { return dataEnd; }
    public String getStatus() { return status; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public void setDescriere(String descriere) { this.descriere = descriere; }
    public void setDataStart(String dataStart) { this.dataStart = dataStart; }
    public void setDataEnd(String dataEnd) { this.dataEnd = dataEnd; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return titlu; }
}