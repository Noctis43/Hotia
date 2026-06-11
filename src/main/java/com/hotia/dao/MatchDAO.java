package com.hotia.dao;

import com.hotia.model.Match;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class MatchDAO {

    private Match mapMatch(ResultSet rs) throws SQLException {
        
        Match m = new Match();
        m.setId(rs.getInt("id"));
        m.setUtilisateur1Id(rs.getInt("utilisateur1_id"));
        m.setUtilisateur2Id(rs.getInt("utilisateur2_id"));
        m.setActif(rs.getInt("actif"));
        Timestamp ts = rs.getTimestamp("date_match");
        
        if (ts != null) {
            m.setDateMatch(ts.toLocalDateTime());
        }
        
        return m;
    }

    /** Crée un nouveau match */
    public int creerMatch(Match match) throws SQLException {
        
        String sql = "INSERT INTO matchs (utilisateur1_id, utilisateur2_id, actif) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, match.getUtilisateur1Id());
            ps.setInt(2, match.getUtilisateur2Id());
            ps.setInt(3, match.getActif());
            ps.executeUpdate();
        
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Recherche le match actif d'un utilisateur. */
    public Match trouverMatchActifParUtilisateur(int utilisateurId) throws SQLException {
        
        String sql = "SELECT * FROM matchs WHERE actif = 1 "
                + "AND (utilisateur1_id = ? OR utilisateur2_id = ?) LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, utilisateurId);
        
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMatch(rs);
                }
            }
        }
        return null;
    }

    /** Désactive un match (actif = 0). */
    public void desactiverMatch(int matchId) throws SQLException {
        
        String sql = "UPDATE matchs SET actif = 0 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            ps.executeUpdate();
        }
    }

    /** Recherche un match par id. */
    public Match trouverParId(int matchId) throws SQLException {
        String sql = "SELECT * FROM matchs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMatch(rs);
                }
            }
        }
        return null;
    }
}
