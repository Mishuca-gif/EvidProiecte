package com.evidenta.dao;

import com.evidenta.model.Sarcina;
import com.evidenta.model.StatusSarcina;
import com.evidenta.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SarcinaDAO {

    private static final String SELECT_JOIN =
        "SELECT s.*, p.titlu AS numeProiect, " +
        "CONCAT(e.prenume, ' ', e.nume) AS numeExecutant " +
        "FROM Sarcini s " +
        "JOIN Proiecte p ON s.id_proiect = p.id " +
        "LEFT JOIN Executanti e ON s.id_executant = e.id ";

    public void add(Sarcina s) throws SQLException {
        String sql = "INSERT INTO Sarcini (titlu, descriere, prioritate, status, id_proiect, id_executant) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTitlu());
            ps.setString(2, s.getDescriere());
            ps.setInt(3, s.getPrioritate());
            ps.setString(4, s.getStatus().name());
            ps.setInt(5, s.getIdProiect());
            ps.setInt(6, s.getIdExecutant());
            ps.executeUpdate();
        }
    }

    public List<Sarcina> getAll() throws SQLException {
        List<Sarcina> list = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SELECT_JOIN)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Sarcina> getByProiect(int idProiect) throws SQLException {
        List<Sarcina> list = new ArrayList<>();
        String sql = SELECT_JOIN + "WHERE s.id_proiect = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProiect);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Sarcina> search(String keyword, String status) throws SQLException {
        List<Sarcina> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SELECT_JOIN + "WHERE 1=1 ");
        if (keyword != null && !keyword.isEmpty())
            sql.append("AND s.titlu LIKE ? ");
        if (status != null && !status.isEmpty())
            sql.append("AND s.status = ? ");
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int i = 1;
            if (keyword != null && !keyword.isEmpty())
                ps.setString(i++, "%" + keyword + "%");
            if (status != null && !status.isEmpty())
                ps.setString(i, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void update(Sarcina s) throws SQLException {
        String sql = "UPDATE Sarcini SET titlu=?, descriere=?, prioritate=?, status=?, id_proiect=?, id_executant=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTitlu());
            ps.setString(2, s.getDescriere());
            ps.setInt(3, s.getPrioritate());
            ps.setString(4, s.getStatus().name());
            ps.setInt(5, s.getIdProiect());
            ps.setInt(6, s.getIdExecutant());
            ps.setInt(7, s.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Sarcini WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Sarcina mapRow(ResultSet rs) throws SQLException {
        return new Sarcina(
            rs.getInt("id"),
            rs.getString("titlu"),
            rs.getString("descriere"),
            rs.getInt("prioritate"),
            StatusSarcina.valueOf(rs.getString("status")),
            rs.getInt("id_proiect"),
            rs.getInt("id_executant"),
            rs.getString("numeProiect"),
            rs.getString("numeExecutant")
        );
    }
}