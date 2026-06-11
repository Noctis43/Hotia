package com.hotia.dao;

import com.hotia.model.DemandeMatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class DemandeMatchDAO {

    private DemandeMatch mapDemande(ResultSet rs) throws SQLException {
        
        DemandeMatch d = new DemandeMatch();
        d.setId(rs.getInt("id"));
        d.setDemandeurId(rs.getInt("demandeur_id"));
        d.setCibleId(rs.getInt("cible_id"));
        d.setStatut(rs.getString("statut"));
        Timestamp ts = rs.getTimestamp("date_demande");
        
        if (ts != null) {
            d.setDateDemande(ts.toLocalDateTime());
        }
        
        return d;
    }

    /** Insère nouvelle demande de match. */
    public int creerDemande(DemandeMatch demande) throws SQLException {
        
        String sql = "INSERT INTO demandes_match (demandeur_id, cible_id, statut) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, demande.getDemandeurId());
            ps.setInt(2, demande.getCibleId());
            ps.setString(3, demande.getStatut());
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Liste les demandes en attente reçues par un utilisateur. */
    public List<DemandeMatch> trouverDemandesEnAttentePourUtilisateur(int cibleId) throws SQLException {
        
        String sql = "SELECT * FROM demandes_match WHERE cible_id = ? AND statut = 'en_attente' ORDER BY date_demande DESC";
        List<DemandeMatch> liste = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cibleId);
        
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapDemande(rs));
                }
            }
        }
        return liste;
    }

    /** Recherche une demande par id. */
    public DemandeMatch trouverParId(int id) throws SQLException {
        
        String sql = "SELECT * FROM demandes_match WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
        
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapDemande(rs);
                }
            }
        }
        return null;
    }

    /** UPDATE statut d'une demande. */
    public void mettreAJourStatut(int demandeId, String statut) throws SQLException {
        
        String sql = "UPDATE demandes_match SET statut = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, demandeId);
            ps.executeUpdate();
        }
    }

    /** Refuse toutes les autres demandes en attente impliquant les deux utilisateurs. */
    public void refuserAutresDemandesEnAttente(int utilisateur1Id, int utilisateur2Id) throws SQLException {
        
        String sql = "UPDATE demandes_match SET statut = 'refusée' WHERE statut = 'en_attente' "
                + "AND ((demandeur_id = ? OR cible_id = ?) OR (demandeur_id = ? OR cible_id = ?)) "
                + "AND NOT (demandeur_id = ? AND cible_id = ?) "
                + "AND NOT (demandeur_id = ? AND cible_id = ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateur1Id);
            ps.setInt(2, utilisateur1Id);
            ps.setInt(3, utilisateur2Id);
            ps.setInt(4, utilisateur2Id);
            ps.setInt(5, utilisateur1Id);
            ps.setInt(6, utilisateur2Id);
            ps.setInt(7, utilisateur2Id);
            ps.setInt(8, utilisateur1Id);
            ps.executeUpdate();
        }
    }

    /** Vérifie si une demande existe déjà entre deux utilisateurs. */
    public boolean existeDemande(int demandeurId, int cibleId) throws SQLException {
        
        String sql = "SELECT COUNT(*) AS nb FROM demandes_match WHERE demandeur_id = ? AND cible_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, demandeurId);
            ps.setInt(2, cibleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("nb") > 0;
                }
            }
        }
        return false;
    }
}
