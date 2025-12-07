package com.compagnie.dao;

import com.compagnie.models.Passager;
import com.compagnie.models.TypePassager;
import com.compagnie.models.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class PassagerDAO implements IGenericDAO<Passager, Integer> {

    private Passager mapResultSetToPassager(ResultSet rs) throws SQLException {
        Passager p = new Passager();
        p.setIdentificateurPassager(rs.getInt("IdentificateurPassager"));
        p.setNom(rs.getString("Nom"));
        p.setPrenom(rs.getString("Prenom"));

        Date dateNaissance = rs.getDate("DateNaissance");
        if (dateNaissance != null) {
            p.setDateNaissance(new Date(dateNaissance.getTime()));
        }

        p.setNumPasseport(rs.getString("NumPasseport"));
        p.setNationalite(rs.getString("Nationalite"));

        p.setTypePassager(TypePassager.valueOf(rs.getString("TypePassager")));
        p.setGenre(Genre.valueOf(rs.getString("Genre")));

        p.setEmail(rs.getString("Email"));
        p.setTelephone(rs.getString("Telephone"));
        return p;
    }

    @Override
    public Passager chercher(Integer id) throws SQLException {
        String sql = "SELECT * FROM Passager WHERE IdentificateurPassager = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPassager(rs);
            }
            return null;
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
    }

    @Override
    public boolean ajouter(Passager p) throws SQLException {
        String sql = "INSERT INTO Passager (Nom, Prenom, DateNaissance, NumPasseport, Nationalite, TypePassager, Genre, Email, Telephone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
            pstmt.setString(4, p.getNumPasseport());
            pstmt.setString(5, p.getNationalite());
            pstmt.setString(6, p.getTypePassager().name());
            pstmt.setString(7, p.getGenre().name());
            pstmt.setString(8, p.getEmail());
            pstmt.setString(9, p.getTelephone());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    p.setIdentificateurPassager(rs.getInt(1));
                }
            }
            return affectedRows > 0;
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
    }

    @Override
    public boolean modifier(Passager p) throws SQLException {
        String sql = "UPDATE Passager SET Nom=?, Prenom=?, DateNaissance=?, NumPasseport=?, Nationalite=?, TypePassager=?, Genre=?, Email=?, Telephone=? WHERE IdentificateurPassager=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
            pstmt.setString(4, p.getNumPasseport());
            pstmt.setString(5, p.getNationalite());
            pstmt.setString(6, p.getTypePassager().name());
            pstmt.setString(7, p.getGenre().name());
            pstmt.setString(8, p.getEmail());
            pstmt.setString(9, p.getTelephone());

            pstmt.setInt(10, p.getIdentificateurPassager());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBManager.close(pstmt, conn);
        }
    }

    @Override
    public boolean supprimer(Integer id) throws SQLException {
        String sql = "DELETE FROM Passager WHERE IdentificateurPassager = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBManager.close(pstmt, conn);
        }
    }

    @Override
    public List<Passager> listerTous() throws SQLException {
        List<Passager> passagers = new ArrayList<>();
        String sql = "SELECT * FROM Passager ORDER BY Nom, Prenom";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                passagers.add(mapResultSetToPassager(rs));
            }
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
        return passagers;
    }
}