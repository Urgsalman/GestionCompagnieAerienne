package com.compagnie.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/CompagnieAérienne";
    private static final String USER = "postgres";
    private static final String PASSWORD = "31B7DE07EF";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL introuvable.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    System.err.println("Erreur lors de la fermeture de la ressource : " + e.getMessage());
                }
            }
        }
    }
}