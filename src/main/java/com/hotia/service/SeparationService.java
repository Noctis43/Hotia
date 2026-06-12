package com.hotia.service;

import com.hotia.dao.MatchDAO;
import com.hotia.dao.SeparationDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Match;
import com.hotia.model.Separation;
import com.hotia.model.Utilisateur;

import java.sql.SQLException;

public class SeparationService {

    private final SeparationDAO separationDAO = new SeparationDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final MessageService messageService = new MessageService();

    public String demanderSeparation(int utilisateurId) throws SQLException {

        Utilisateur u = utilisateurDAO.trouverParId(utilisateurId);

        if (u == null || !"en_matching".equals(u.getStatut())) {
            return "Vous devez être en matching pour demander une séparation.";
        }

        Match match = matchDAO.trouverMatchActifParUtilisateur(utilisateurId);
        
        if (match == null) {
            return "aucun match actif.";
        }

        Separation separation = new Separation();
        separation.setMatchId(match.getId());
        separation.setDemandeurId(utilisateurId);
        separation.setStatut("en_attente");
        separationDAO.creerSeparation(separation);
        return null;
    }

    public String confirmerSeparation(int separationId, int utilisateurId) throws SQLException {

        Separation separation = trouverSeparation(separationId);

        if (separation == null) {
            return "Séparation introuvable.";
        }

        Match match = matchDAO.trouverParId(separation.getMatchId());

        if (match == null) {
            return "Match introuvable.";
        }

        if (separation.getDemandeurId() == utilisateurId) {
            return "Vous ne pouvez pas confirmer votre propre demande.";
        }

        if (!utilisateurEstDansMatch(utilisateurId, match)) {
            return "Action non autorisée.";
        }

        separationDAO.mettreAJourStatut(separationId, "confirmée");
        matchDAO.desactiverMatch(match.getId());
        utilisateurDAO.mettreAJourStatut(match.getUtilisateur1Id(), "libre");
        utilisateurDAO.mettreAJourStatut(match.getUtilisateur2Id(), "libre");
        return null;
    }

    public void refuserSeparation(int separationId, int utilisateurId) throws SQLException {

        Separation separation = trouverSeparation(separationId);

        if (separation == null) {
            return;
        }

        Match match = matchDAO.trouverParId(separation.getMatchId());

        if (match != null && utilisateurEstDansMatch(utilisateurId, match) && separation.getDemandeurId() != utilisateurId) {
            separationDAO.mettreAJourStatut(separationId, "refusée");
        }
    }

    public Separation trouverSeparationEnAttente(int utilisateurId) throws SQLException {
        
        Match match = matchDAO.trouverMatchActifParUtilisateur(utilisateurId);
        
        if (match == null) {
            return null;
        }
        return separationDAO.trouverSeparationEnAttenteParMatch(match.getId());
    }

    public String trouverPrenomPartenaire(int utilisateurId) throws SQLException {
        
        int partenaireId = messageService.trouverPartenaireId(utilisateurId);
        
        if (partenaireId < 0) {
            return "";
        }
        
        Utilisateur partenaire = utilisateurDAO.trouverParId(partenaireId);
        return partenaire != null ? partenaire.getPrenom() : "";
    }

    private Separation trouverSeparation(int separationId) throws SQLException {
        return separationDAO.trouverParId(separationId);
    }

    private boolean utilisateurEstDansMatch(int utilisateurId, Match match) {
        return match.getUtilisateur1Id() == utilisateurId || match.getUtilisateur2Id() == utilisateurId;
    }
}