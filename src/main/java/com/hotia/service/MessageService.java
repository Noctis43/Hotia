package com.hotia.service;

import com.hotia.dao.MatchDAO;
import com.hotia.dao.MessageDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Match;
import com.hotia.model.Message;
import com.hotia.model.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private final MessageDAO messageDAO = new MessageDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<Message> chargerHistorique(int utilisateurId) throws SQLException {

        Match match = matchDAO.trouverMatchActifParUtilisateur(utilisateurId);

        if (match == null) {
            return List.of();
        }
        return messageDAO.trouverParMatchId(match.getId());
    }

    public String envoyerMessage(int utilisateurId, String contenu) throws SQLException {

        if (contenu == null || contenu.trim().isEmpty()) {
            return "le message ne peut pas être vide.";
        }

        Utilisateur u = utilisateurDAO.trouverParId(utilisateurId);

        if (u == null || !"en_matching".equals(u.getStatut())) {
            return "Vous devez être en matching pour envoyer un message.";
        }

        Match match = matchDAO.trouverMatchActifParUtilisateur(utilisateurId);

        if (match == null) {
            return "Aucun match actif trouvé.";
        }

        Message message = new Message();
        message.setMatchId(match.getId());
        message.setEmetteurId(utilisateurId);
        message.setContenu(contenu.trim());
        messageDAO.insererMessage(message);
        return null;
    }

    public int trouverPartenaireId(int utilisateurId) throws SQLException {

        Match match = matchDAO.trouverMatchActifParUtilisateur(utilisateurId);

        if (match == null) {
            return -1;
        }

        if (match.getUtilisateur1Id() == utilisateurId) {
            return match.getUtilisateur2Id();
        }
        return match.getUtilisateur1Id();
    }

     public Match trouverMatchActif(int utilisateurId) throws SQLException {
        return matchDAO.trouverMatchActifParUtilisateur(utilisateurId);
    }
}