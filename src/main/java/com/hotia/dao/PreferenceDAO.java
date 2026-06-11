package com.hotia.dao;

import com.hotia.model.Preference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PreferenceDAO {

    private Preference mapPreferenceBase(ResultSet rs) throws SQLException {
        
        Preference p = new Preference();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setType(rs.getString("type"));
        p.setSexeCible(rs.getString("sexe_cible"));
        p.setSituationFamiliale(rs.getString("situation_familiale"));
        p.setTypeRelation(rs.getString("type_relation"));
        int ageMin = rs.getInt("age_min");
        
        if (!rs.wasNull()) {
            p.setAgeMin(ageMin);
        }
        
        int ageMax = rs.getInt("age_max");
        
        if (!rs.wasNull()) {
            p.setAgeMax(ageMax);
        }
        
        return p;
    }

    /** Charge une préférence complète avec divertissements et comportements. */
    public Preference trouverParUtilisateurEtType(int utilisateurId, String type) throws SQLException {
        
        String sql = "SELECT * FROM preferences WHERE utilisateur_id = ? AND type = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, utilisateurId);
            ps.setString(2, type);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Preference p = mapPreferenceBase(rs);
                    p.setDivertissements(trouverDivertissementsByPreferenceId(p.getId()));
                    p.setComportements(trouverComportementsByPreferenceId(p.getId()));
                    return p;
                }
            }
        }
        return null;
    }

    /** Insère une préférence  */
    public int creerPreference(Preference preference) throws SQLException {
        
        String sql = "INSERT INTO preferences (utilisateur_id, type, sexe_cible, situation_familiale, "
                + "type_relation, age_min, age_max) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, preference.getUtilisateurId());
            ps.setString(2, preference.getType());
            ps.setString(3, preference.getSexeCible());
            ps.setString(4, preference.getSituationFamiliale());
            ps.setString(5, preference.getTypeRelation());
            
            if (preference.getAgeMin() != null) {
                ps.setInt(6, preference.getAgeMin());
            } else {
                ps.setNull(6, java.sql.Types.TINYINT);
            }
            
            if (preference.getAgeMax() != null) {
                ps.setInt(7, preference.getAgeMax());
            } else {
                ps.setNull(7, java.sql.Types.TINYINT);
            }
            
            ps.executeUpdate();
            
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** UPDATE les champs scalaires d'une préférence. */
    public void mettreAJourPreference(Preference preference) throws SQLException {
        
        String sql = "UPDATE preferences SET sexe_cible = ?, situation_familiale = ?, type_relation = ?, "
                + "age_min = ?, age_max = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, preference.getSexeCible());
            ps.setString(2, preference.getSituationFamiliale());
            ps.setString(3, preference.getTypeRelation());
            
            if (preference.getAgeMin() != null) {
                ps.setInt(4, preference.getAgeMin());
            } else {
                ps.setNull(4, java.sql.Types.TINYINT);
            }
            
            if (preference.getAgeMax() != null) {
                ps.setInt(5, preference.getAgeMax());
            } else {
                ps.setNull(5, java.sql.Types.TINYINT);
            }
            
            ps.setInt(6, preference.getId());
            ps.executeUpdate();
        }
    }

    /** Retourne les libellés des divertissements liés à une préférence. */
    public List<String> trouverDivertissementsByPreferenceId(int preferenceId) throws SQLException {
        
        String sql = "SELECT rd.libelle FROM preferences_divertissements pd "
                + "JOIN ref_divertissements rd ON pd.divertissement_id = rd.id "
                + "WHERE pd.preference_id = ?";
        List<String> liste = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(rs.getString("libelle"));
                }
            }
        }
        return liste;
    }

    /** Retourne les libellés des comportements liés à une préférence. */
    public List<String> trouverComportementsByPreferenceId(int preferenceId) throws SQLException {
        
        String sql = "SELECT rc.libelle FROM preferences_comportements pc "
                + "JOIN ref_comportements rc ON pc.comportement_id = rc.id "
                + "WHERE pc.preference_id = ?";
        List<String> liste = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(rs.getString("libelle"));
                }
            }
        }
        return liste;
    }

    /** DELETE les liaisons divertissements d'une préférence. */
    public void supprimerDivertissementsByPreferenceId(int preferenceId) throws SQLException {
        
        String sql = "DELETE FROM preferences_divertissements WHERE preference_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            ps.executeUpdate();
        }
    }

    /** DELETE les liaisons comportements d'une préférence. */
    public void supprimerComportementsByPreferenceId(int preferenceId) throws SQLException {
        
        String sql = "DELETE FROM preferences_comportements WHERE preference_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            ps.executeUpdate();
        }
    }

    /** Insère une liaison préférence-divertissement. */
    public void insererDivertissement(int preferenceId, int divertissementId) throws SQLException {
        
        String sql = "INSERT INTO preferences_divertissements (preference_id, divertissement_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            ps.setInt(2, divertissementId);
            ps.executeUpdate();
        }
    }

    /** Insère une liaison préférence-comportement. */
    public void insererComportement(int preferenceId, int comportementId) throws SQLException {
        
        String sql = "INSERT INTO preferences_comportements (preference_id, comportement_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, preferenceId);
            ps.setInt(2, comportementId);
            ps.executeUpdate();
        }
    }

    /** Retourne la map de tous les divertissements de référence. */
    public Map<Integer, String> trouverTousDivertissementsRef() throws SQLException {
        
        String sql = "SELECT id, libelle FROM ref_divertissements ORDER BY id";
        Map<Integer, String> map = new LinkedHashMap<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("libelle"));
            }
        }
        return map;
    }

    /** Retourne la map de tous les comportements de référence. */
    public Map<Integer, String> trouverTousComportementsRef() throws SQLException {
        
        String sql = "SELECT id, libelle FROM ref_comportements ORDER BY id";
        Map<Integer, String> map = new LinkedHashMap<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id"), rs.getString("libelle"));
            }
        }
        return map;
    }
}
