package com.evidenta.model;

public abstract class Persoana {
    protected int id;
    protected String nume;
    protected String prenume;
    protected String email;

    public Persoana(int id, String nume, String prenume, String email) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }

    public abstract String getInfo();

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getEmail() { return email; }
    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setEmail(String email) { this.email = email; }
}