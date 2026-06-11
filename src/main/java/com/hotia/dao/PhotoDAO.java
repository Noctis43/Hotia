package com.hotia.dao;

import com.hotia.model.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class PhotoDAO {


    private Photo mapPhoto(ResultSet rs) throws SQLException {
        
        Photo p = new Photo();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setChemin(rs.getString("chemin"));
        p.setEstPrincipale(rs.getInt("est_principale"));
        Timestamp ts = rs.getTimestamp("date_upload");
        
        if (ts != null) {
            p.setDateUpload(ts.toLocalDateTime());
        }
        
        return p;
    }

    /** Insère photo  */
    public int insererPhoto(Photo photo) throws SQLException {
        
        String sql = "INSERT INTO photos (utilisateur_id, chemin, est_principale) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, photo.getUtilisateurId());
            ps.setString(2, photo.getChemin());
            ps.setInt(3, photo.getEstPrincipale());
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** liste les photos d'un utilisateur. */
    public List<Photo> trouverParUtilisateurId(int utilisateurId) throws SQLException {
        
        String sql = "SELECT * FROM photos WHERE utilisateur_id = ? ORDER BY est_principale DESC, date_upload ASC";
        List<Photo> liste = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapPhoto(rs));
                }
            }
        }
        return liste;
    }

    /** Retourne la photo principale d'un utilisateur. */
    public Photo trouverPhotoPrincipale(int utilisateurId) throws SQLException {
        String sql = "SELECT * FROM photos WHERE utilisateur_id = ? AND est_principale = 1 LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPhoto(rs);
                }
            }
        }
        return null;
    }

    /** DELETE les photos d'un utilisateur. */
    public void supprimerParUtilisateurId(int utilisateurId) throws SQLException {
        
        String sql = "DELETE FROM photos WHERE utilisateur_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.executeUpdate();
        }
    }
}
