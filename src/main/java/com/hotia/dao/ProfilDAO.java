package com.hotia.dao;

import com.hotia.model.Profil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ProfilDAO {


    private Profil mapProfil(ResultSet rs) throws SQLException {

        Profil p = new Profil();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setTelephone(rs.getString("telephone"));
        p.setBio(rs.getString("bio"));
        int villeId = rs.getInt("ville_id");

        if (!rs.wasNull()) {
            p.setVilleId(villeId);
        }

        p.setLatitude(rs.getBigDecimal("latitude"));
        p.setLongitude(rs.getBigDecimal("longitude"));
        return p;
    }

    /** Insère Profil */
    public int creerProfil(Profil profil) throws SQLException {

        String sql = "INSERT INTO profils (utilisateur_id, telephone, bio, ville_id, latitude, longitude) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, profil.getUtilisateurId());
            ps.setString(2, profil.getTelephone());
            ps.setString(3, profil.getBio());

            if (profil.getVilleId() != null) {
                ps.setInt(4, profil.getVilleId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            ps.setBigDecimal(5, profil.getLatitude());
            ps.setBigDecimal(6, profil.getLongitude());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Recherche profil utilisateur. */
    public Profil trouverParUtilisateurId(int utilisateurId) throws SQLException {

        String sql = "SELECT * FROM profils WHERE utilisateur_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProfil(rs);
                }
            }
        }
        return null;
    }

    /** Update profil existant. */
    public void mettreAJourProfil(Profil profil) throws SQLException {
        
        String sql = "UPDATE profils SET telephone = ?, bio = ?, ville_id = ?, latitude = ?, longitude = ? "
                + "WHERE utilisateur_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profil.getTelephone());
            ps.setString(2, profil.getBio());
            
            if (profil.getVilleId() != null) {
                ps.setInt(3, profil.getVilleId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            ps.setBigDecimal(4, profil.getLatitude());
            ps.setBigDecimal(5, profil.getLongitude());
            ps.setInt(6, profil.getUtilisateurId());
            ps.executeUpdate();
        }
    }
}
