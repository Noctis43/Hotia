package com.hotia.dao;

import com.hotia.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {

    private Message mapMessage(ResultSet rs) throws SQLException {
    
        Message m = new Message();
        m.setId(rs.getInt("id"));
        m.setMatchId(rs.getInt("match_id"));
        m.setEmetteurId(rs.getInt("emetteur_id"));
        m.setContenu(rs.getString("contenu"));
        Timestamp ts = rs.getTimestamp("date_envoi");
    
        if (ts != null) {
            m.setDateEnvoi(ts.toLocalDateTime());
        }
    
        return m;
    }

    /** Insère un message  */
    public int insererMessage(Message message) throws SQLException {
        
        String sql = "INSERT INTO messages (match_id, emetteur_id, contenu) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getMatchId());
            ps.setInt(2, message.getEmetteurId());
            ps.setString(3, message.getContenu());
            ps.executeUpdate();
        
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    /** Retourne l'historique des messages. */
    public List<Message> trouverParMatchId(int matchId) throws SQLException {
        
        String sql = "SELECT * FROM messages WHERE match_id = ? ORDER BY date_envoi ASC";
        List<Message> liste = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, matchId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapMessage(rs));
                }
            }
        }
        return liste;
    }
}
