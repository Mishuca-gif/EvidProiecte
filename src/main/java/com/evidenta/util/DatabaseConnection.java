package com.evidenta.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=EvidProiecte;" +
            "user=evidenta;" +
            "password=Parola123!;" +
            "encrypt=false;" +
            "trustServerCertificate=true;";

    private static Connection connection = null;

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexiune reusita!");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la inchidere: " + e.getMessage());
        }
    }
}