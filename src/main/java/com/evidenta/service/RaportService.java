package com.evidenta.service;

import com.evidenta.interfaces.Reportable;
import com.evidenta.util.DatabaseConnection;

import java.sql.*;

public class RaportService implements Reportable {

    @Override
    public String generateReport() {
        return raport1SarciniPerProiect();
    }

    public String raport1SarciniPerProiect() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPORT 1: Sarcini per proiect ===\n");
        String sql = "SELECT p.titlu, COUNT(s.id) AS total " +
                     "FROM Proiecte p LEFT JOIN Sarcini s ON p.id = s.id_proiect " +
                     "GROUP BY p.titlu ORDER BY total DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sb.append("Proiect: ").append(rs.getString("titlu"))
                  .append(" | Sarcini: ").append(rs.getInt("total")).append("\n");
            }
        } catch (SQLException e) {
            sb.append("Eroare: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public String raport2ExecutantiActivi() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPORT 2: Executanti si sarcinile active ===\n");
        String sql = "SELECT e.prenume + ' ' + e.nume AS executant, COUNT(s.id) AS total " +
                     "FROM Executanti e LEFT JOIN Sarcini s ON e.id = s.id_executant " +
                     "AND s.status != 'FINALIZATA' " +
                     "GROUP BY e.prenume, e.nume ORDER BY total DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sb.append("Executant: ").append(rs.getString("executant"))
                  .append(" | Sarcini active: ").append(rs.getInt("total")).append("\n");
            }
        } catch (SQLException e) {
            sb.append("Eroare: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public String raport3SarciniDupaStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RAPORT 3: Sarcini dupa status ===\n");
        String sql = "SELECT status, COUNT(*) AS total FROM Sarcini GROUP BY status";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sb.append("Status: ").append(rs.getString("status"))
                  .append(" | Total: ").append(rs.getInt("total")).append("\n");
            }
        } catch (SQLException e) {
            sb.append("Eroare: ").append(e.getMessage());
        }
        return sb.toString();
    }
}