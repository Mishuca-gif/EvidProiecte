package com.evidenta.dao;

import com.evidenta.model.Proiect;
import com.evidenta.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProiectDAO {

    public void add(Proiect p) throws SQLException {
        String sql = "INSERT INTO Proiecte (titlu, descriere, data_start, data_end, status) VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitlu());
            ps.setString(2, p.getDescriere());
            ps.setString(3, p.getDataStart());
            ps.setString(4, p.getDataEnd());
            ps.setString(5, p.getStatus());
            ps.executeUpdate();
        }
    }

    public List<Proiect> getAll() throws SQLException {
        List<Proiect> list = new ArrayList<>();
        String sql = "SELECT * FROM Proiecte";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Proiect getById(int id) throws SQLException {
        String sql = "SELECT * FROM Proiecte WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Proiect> search(String keyword) throws SQLException {
        List<Proiect> list = new ArrayList<>();
        String sql = "SELECT * FROM Proiecte WHERE titlu LIKE ? OR status LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void update(Proiect p) throws SQLException {
        String sql = "UPDATE Proiecte SET titlu=?, descriere=?, data_start=?, data_end=?, status=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitlu());
            ps.setString(2, p.getDescriere());
            ps.setString(3, p.getDataStart());
            ps.setString(4, p.getDataEnd());
            ps.setString(5, p.getStatus());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Proiecte WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Proiect mapRow(ResultSet rs) throws SQLException {
        return new Proiect(
            rs.getInt("id"),
            rs.getString("titlu"),
            rs.getString("descriere"),
            rs.getString("data_start"),
            rs.getString("data_end"),
            rs.getString("status")
        );
    }
}