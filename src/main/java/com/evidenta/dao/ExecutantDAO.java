package com.evidenta.dao;

import com.evidenta.model.Executant;
import com.evidenta.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExecutantDAO {

    public void add(Executant e) throws SQLException {
        String sql = "INSERT INTO Executanti (nume, prenume, email, rol) VALUES (?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNume());
            ps.setString(2, e.getPrenume());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getRol());
            ps.executeUpdate();
        }
    }

    public List<Executant> getAll() throws SQLException {
        List<Executant> list = new ArrayList<>();
        String sql = "SELECT * FROM Executanti";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Executant(
                    rs.getInt("id"),
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("email"),
                    rs.getString("rol")
                ));
            }
        }
        return list;
    }

    public Executant getById(int id) throws SQLException {
        String sql = "SELECT * FROM Executanti WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Executant(
                    rs.getInt("id"),
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("email"),
                    rs.getString("rol")
                );
            }
        }
        return null;
    }

    public void update(Executant e) throws SQLException {
        String sql = "UPDATE Executanti SET nume=?, prenume=?, email=?, rol=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNume());
            ps.setString(2, e.getPrenume());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getRol());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Executanti WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}