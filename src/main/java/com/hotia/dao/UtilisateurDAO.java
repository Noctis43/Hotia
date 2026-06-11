package com.hotia.dao;

import com.hotia.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    private Utilisateur mapUtilisateur (ResultSet rs) throws SQLException {

        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));

        if (rs.getDate("date_naissance") != null) {
            u.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        }

        u.setSexe(rs.getString("sexe"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setStatut(rs.getString("statut"));
        u.setProfileComplet(rs.getInt("profil_complet"));
        Timestamp ts = rs.getTimestamp("date_inscription");

        if (ts != null) {
            u.setDateInscription(ts.toLocalDateTime());
        }
        return u;
    }

    /**Insert nouvel Utilisateur */
    public int creerUtilisateur(Utilisateur utilisateur) throws SQLException {

        String sql = "INSERT INTO utilisateurs (nom, prenom, email, date_naissance, sexe, mot_de_passe, statut, profile_complet) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(utilisateur.getDateNaissance()));
            ps.setString(5, utilisateur.getSexe());
            ps.setString(6, utilisateur.getMotDePasse());
            ps.setString(7, utilisateur.getStatut());
            ps.setInt(8, utilisateur.getProfileComplet());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            } 
        }
        return -1;
    }

    /**trouver utilisateur par son email */
    public Utilisateur trouverParEmail(String email) throws SQLException {

        String sql = "SELECT * FROM utilisateurs WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUtilisateur(rs);
                }
            }
        }
        return null;
    }

    /**Auth un utilisateur par email et mot de passe haché */
    public Utilisateur trouverParEmailEtMotDePasse(String email, String motDePasseHash) throws SQLException {

        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasseHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUtilisateur(rs);
                }
            }
        }
        return null;
    }

    /**trouver utilisateur par son id */
    public Utilisateur trouverParId(int id) throws SQLException {

        String sql = "SELECT * FROM utilisateurs WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUtilisateur(rs);
                }
            }
        }
        return null;
    }

    /**Update statut utilisateur */
    public void mettreAJourStatut(int utilisateurId, String statut) throws SQLException {

        String sql = "UPDATE utilisateurs SET statut = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, utilisateurId);
            ps.executeUpdate();
        }
    }

    /**Update profil_complet */
    public void mettreAJourProfilComplet(int utilisateurId, int profilComplet) throws SQLException {

        String sql = "UPDATE utilisateurs SET profil_complet = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, profilComplet);
            ps.setInt(2, utilisateurId);
            ps.executeUpdate();    
        }
    }

    /**Liste des utilisateur eligible pour Decouvrir */
    public List<Utilisateur> trouverCandidatsDecouvrir(int utilisateurCourantId) throws SQLException {

        String sql = "SELECT u.* FROM utilisateurs u "
                + "WHERE u.id <> ? AND u.profil_complet = 1 AND u.statut = 'libre' "
                + "AND u.id NOT IN (SELECT cible_id FROM swipes_passes WHERE utilisateur_id = ?) "
                + "AND u.id NOT IN (SELECT cible_id FROM demandes_match WHERE demandeur_id = ?) "
                + "AND u.id NOT IN (SELECT demandeur_id FROM demandes_match WHERE cible_id = ?)";
        List<Utilisateur> liste = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurCourantId);
            ps.setInt(2, utilisateurCourantId);
            ps.setInt(3, utilisateurCourantId);
            ps.setInt(4, utilisateurCourantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapUtilisateur(rs));
                }
            }
        }
        return liste;
    }

    /**Enregistre les swipe de droite */
    public void enregistrerSwipePasse(int utilisateurId, int cibleId) throws SQLException {

        String sql = "INSERT INTO swipes_passes (utilisateur_id, cible_id) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE date_swipe = CURRENT_TIMESTAMP";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setInt(2, cibleId);
            ps.executeUpdate();
        }
    }
}