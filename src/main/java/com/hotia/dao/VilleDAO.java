package com.hotia.dao;

import com.hotia.model.Ville;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VilleDAO {

    private Vile mapVille(ResultSet rs) throws SQLException {

        Ville v = new Ville();
        v.setId(rs.getInt("id"));
        v.setNom(rs.getString("nom"));
        v.setLatitude(rs.getBigDecimal("latitude"));
        v.setLongitude(rs.getBigDecimal("longitude"));
        return v;
    }

    /** Toute les ville triée par Nom */
    public List<Ville> trouverToutes() throws SQLException {

        String sql = "SELECT * FROM villes ORDER BY nom";
        List<ville> liste = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.PreparedStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapVille(rs));
                }
            }
        return liste;
    }

    /** Ville par ID */
    public Ville trouverParID(int id) throws SQLException {

        String sql = "SELECT * FROM villes WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.preparedStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapVille(rs);
                    }
                }  
            }
        return null;
    }
}