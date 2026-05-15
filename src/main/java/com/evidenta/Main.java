package com.evidenta;

import com.evidenta.dao.*;
import com.evidenta.model.*;
import com.evidenta.service.*;
import com.evidenta.util.DatabaseConnection;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.getConnection();

            ProiectDAO proiectDAO       = new ProiectDAO();
            ExecutantDAO executantDAO   = new ExecutantDAO();
            SarcinaDAO sarcinaDAO       = new SarcinaDAO();
            RaportService raportService = new RaportService();
            ExportService exportService = new ExportService();

            System.out.println("\n--- PROIECTE ---");
            List<Proiect> proiecte = proiectDAO.getAll();
            proiecte.forEach(p -> System.out.println(p.toTxt()));

            System.out.println("\n--- EXECUTANTI ---");
            List<Executant> executanti = executantDAO.getAll();
            executanti.forEach(e -> System.out.println(e.getInfo()));

            System.out.println("\n--- SARCINI ---");
            List<Sarcina> sarcini = sarcinaDAO.getAll();
            sarcini.forEach(s -> System.out.println(s.toTxt()));

            System.out.println("\n" + raportService.raport1SarciniPerProiect());
            System.out.println(raportService.raport2ExecutantiActivi());
            System.out.println(raportService.raport3SarciniDupaStatus());

            exportService.exportCsv(proiecte, "id,titlu,descriere,dataStart,dataEnd,status", "proiecte.csv");
            exportService.exportTxt(sarcini, "Lista Sarcini", "sarcini.txt");
System.out.println("\n--- CAUTARE 'Sistem' ---");
proiectDAO.search("Sistem").forEach(p -> System.out.println(p.toTxt()));
System.out.println("\n---SARCINI IN_PROCES ---");
sarcinaDAO.search("", "IN_PROCES").forEach(s -> System.out.println(s.toTxt()));
System.out.println("\n--- CAUTARE 'API' ---");
sarcinaDAO.search("API", "").forEach(s -> System.out.println(s.toTxt()));
System.out.println("\n--- SARCINI PROIECT 1 ---");
sarcinaDAO.getByProiect(1).forEach(s -> System.out.println(s.toTxt()));
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}