package com.compagnie.dao;

import com.compagnie.models.Billet;
import com.compagnie.models.ClasseBillet;
import com.compagnie.models.StatutBillet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BilletDAO implements IGenericDAO<Billet, String> {

    private Billet mapResultSetToBillet(ResultSet rs) throws SQLException {
        Billet b = new Billet();
        b.setNumBillet(rs.getString("NumBillet"));
        b.setIdentificateurPassager(rs.getInt("IdentificateurPassager"));
        b.setNumVol(rs.getString("NumVol"));
        b.setNumSiege(rs.getString("NumSiege"));
        b.setTarif(rs.getDouble("Tarif"));

        b.setClasse(ClasseBillet.valueOf(rs.getString("Classe")));
        b.setStatut(StatutBillet.valueOf(rs.getString("Statut")));

        Date dateEmission = rs.getDate("DateEmission");
        if (dateEmission != null) {
            b.setDateEmission(new Date(dateEmission.getTime()));
        }
        return b;
    }

    @Override
    public Billet chercher(String numBillet) throws SQLException {
        String sql = "SELECT * FROM Billet WHERE NumBillet = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numBillet);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBillet(rs);
            }
            return null;
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
    }

    @Override
    public boolean ajouter(Billet b) throws SQLException {
        String sql = "INSERT INTO Billet (NumBillet, IdentificateurPassager, NumVol, NumSiege, Classe, Tarif, Statut, DateEmission) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, b.getNumBillet());
            pstmt.setInt(2, b.getIdentificateurPassager());
            pstmt.setString(3, b.getNumVol());
            pstmt.setString(4, b.getNumSiege());
            pstmt.setString(5, b.getClasse().name());
            pstmt.setDouble(6, b.getTarif());
            pstmt.setString(7, b.getStatut().name());
            pstmt.setDate(8, new java.sql.Date(b.getDateEmission().getTime()));

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBManager.close(pstmt, conn);
        }
    }

    @Override
    public boolean modifier(Billet b) throws SQLException {
        String sql = "UPDATE Billet SET IdentificateurPassager=?, NumVol=?, NumSiege=?, Classe=?, Tarif=?, Statut=?, DateEmission=? WHERE NumBillet=?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, b.getIdentificateurPassager());
            pstmt.setString(2, b.getNumVol());
            pstmt.setString(3, b.getNumSiege());
            pstmt.setString(4, b.getClasse().name());
            pstmt.setDouble(5, b.getTarif());
            pstmt.setString(6, b.getStatut().name());
            pstmt.setDate(7, new java.sql.Date(b.getDateEmission().getTime()));

            pstmt.setString(8, b.getNumBillet());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBManager.close(pstmt, conn);
        }
    }

    @Override
    public boolean supprimer(String numBillet) throws SQLException {
        String sql = "DELETE FROM Billet WHERE NumBillet = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numBillet);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } finally {
            DBManager.close(pstmt, conn);
        }
    }

    @Override
    public List<Billet> listerTous() throws SQLException {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM Billet ORDER BY DateEmission DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                billets.add(mapResultSetToBillet(rs));
            }
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
        return billets;
    }
}