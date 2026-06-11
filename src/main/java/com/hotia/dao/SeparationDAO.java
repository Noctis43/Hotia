package com.hotia.dao;

import com.hotia.model.Separation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class SeparationDAO {

    private Separation mapSeparation(ResultSet rs) throws SQLException {
    
        Separation s = new Separation();
        s.setId(rs.getInt("id"));
        s.setMatchId(rs.getInt("match_id"));
        s.setDemandeurId(rs.getInt("demandeur_id"));
        s.setStatut(rs.getString("statut"));
        Timestamp ts = rs.getTimestamp("date_demande");
    
        if (ts != null) {
            s.setDateDemande(ts.toLocalDateTime());
        }
    
        return s;
    }

    /** Insère une demande de séparation. */
    public int creerSeparation(Separation separation) throws SQLException {
    
        String sql = "INSERT INTO separations (match_id, demandeur_id, statut) VALUES (?, ?, ?)";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, separation.getMatchId());
            ps.setInt(2, separation.getDemandeurId());
            ps.setString(3, separation.getStatut());
            ps.executeUpdate();
    
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Recherche une séparation en attente pour un match donné. */
    public Separation trouverSeparationEnAttenteParMatch(int matchId) throws SQLException {
    
        String sql = "SELECT * FROM separations WHERE match_id = ? AND statut = 'en_attente' LIMIT 1";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, matchId);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSeparation(rs);
                }
            }
        }
        return null;
    }

    /** Recherche une séparation par id */
    public Separation trouverParId(int separationId) throws SQLException {
        
        String sql = "SELECT * FROM separations WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, separationId);
        
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSeparation(rs);
                }
            }
        }
        return null;
    }

    /** UPDATE statut d'une séparation. */
    public void mettreAJourStatut(int separationId, String statut) throws SQLException {
        
        String sql = "UPDATE separations SET statut = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, separationId);
            ps.executeUpdate();
        }
    }
}
