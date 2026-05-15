package com.evidenta.service;

import com.evidenta.interfaces.Exportable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportService {

    public <T extends Exportable> void exportCsv(List<T> list, String header, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(header + "\n");
            for (T item : list) {
                fw.write(item.toCsv() + "\n");
            }
        }
        System.out.println("Export CSV reusit: " + filePath);
    }

    public <T extends Exportable> void exportTxt(List<T> list, String titlu, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write("=== " + titlu + " ===\n\n");
            for (T item : list) {
                fw.write(item.toTxt() + "\n");
            }
        }
        System.out.println("Export TXT reusit: " + filePath);
    }

    public void exportRaportTxt(String continut, String filePath) throws IOException {
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(continut);
        }
        System.out.println("Raport exportat: " + filePath);
    }
}