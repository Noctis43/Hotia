package com.hotia.service;

import com.hotia.dao.DemandeMatchDAO;
import com.hotia.dao.MatchDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.DemandeMatch;
import com.hotia.model.Match;
import com.hotia.model.Utilisateur;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchService {

    /**Vue d'une demande avec info demandeur */
    public static class DemandeAvecInfos {
        private DemandeMatch demande;
        private Utilisateur demandeur;
        private String cheminPhoto;
        private String nomVille;
        private int age;
        private double score;

        public DemandeMatch getDemande() {
            return demande;
        }

        public void setDemande(DemandeMatch demande) {
            this.demande = demande;
        }

        public Utilisateur getDemandeur() {
            return demandeur;
        }

        public void setDemandeur(Utilisateur demandeur) {
            this.demandeur = demandeur;
        }

        public String getCheminPhoto() {
            return cheminPhoto;
        }

        public void setCheminPhoto(String cheminPhoto) {
            this.cheminPhoto = cheminPhoto;
        }

        public String getNomVille() {
            return nomVille;
        }

        public void setNomVille(String nomVille) {
            this.nomVille = nomVille;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    private final DemandeMatchDAO demandeMatchDAO = new DemandeMatchDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final com.hotia.dao.PhotoDAO photoDAO = new com.hotia.dao.PhotoDAO();
    private final com.hotia.dao.ProfilDAO profilDAO = new com.hotia.dao.ProfilDAO();
    private final com.hotia.dao.VilleDAO villeDAO = new com.hotia.dao.VilleDAO();
    private final CompatibiliteService compatibiliteService = new CompatibiliteService();
    private final AuthService authService = new AuthService();

    public String envoyerDemande( int demandeurId, int cibleId) throws SQLException {

        if (demandeurId == cibleId) {
            return "Action invalide.";
        }

        if (demandeMatchDAO.existeDemande(demandeurId, cibleId)) {
            return "Demande déjà envoyée.";
        }

        DemandeMatch demande = new DemandeMatch();
        demande.setDemandeurId(demandeurId);
        demande.setCibleId(cibleId);
        demande.setStatut("en_attente");
        demandeMatchDAO.creerDemande(demande);
        return null;
    }

    public String accepterDemande(int demandeId, int utilisateurCourantId) throws SQLException {

        DemandeMatch demande = demandeMatchDAO.trouverParId(demandeId);

        if (demande == null || demande.getCibleId() != utilisateurCourantId) {
            return "Demande introuvable.";
        }

        if (!"en_attente".equals(demande.getStatut())) {
            return "Cette demande n'est plus en attente.";
        }

        Utilisateur courant = utilisateurDAO.trouverParId(utilisateurCourantId);

        if (courant == null || "libre".equals(courant.getStatut())) {
            return "Vous devez être libre pour accepter une demande.";
        }

        demandeMatchDAO.mettreAJourStatut(demandeId, "acceptée");

        Match match = new Match();
        match.setUtilisateur1Id(demande.getDemandeurId());
        match.setUtilisateur2Id(demande.getCibleId());
        match.setActif(1);
        matchDAO.creerMatch(match);

        utilisateurDAO.mettreAJourStatut(demande.getDemandeurId(), "en_matching");
        utilisateurDAO.mettreAJourStatut(demande.getCibleId(), "en_matching");

        demandeMatchDAO.refuserAutresDemandesEnAttente(demande.getDemandeurId(), demande.getCibleId());
        return null;
    }

    public void refuserDemande(int demandeId, int utilisateurCourantId) throws SQLException {
        
        DemandeMatch demande = demandeMatchDAO.trouverParId(demandeId);
        
        if (demande != null && demande.getCibleId() == utilisateurCourantId) {
            demandeMatchDAO.mettreAJourStatut(demandeId, "refusée");
        }
    }

    public List<DemandeAvecInfos> listerDemandesRecues(int cibleId) throws SQLException {
        
        List<DemandeMatch> demandes = demandeMatchDAO.trouverDemandesEnAttentePourUtilisateur(cibleId);
        List<DemandeAvecInfos> resultat = new ArrayList<>();

        for (DemandeMatch d : demandes) {
            DemandeAvecInfos info = new DemandeAvecInfos();
            info.setDemande(d);
            Utilisateur demandeur = utilisateurDAO.trouverParId(d.getDemandeurId());
            info.setDemandeur(demandeur);
            
            if (demandeur != null) {
                info.setAge(authService.calculerAge(demandeur.getDateNaissance()));
                info.setScore(compatibiliteService.calculerScore(cibleId, demandeur.getId()));
                com.hotia.model.Photo photo = photoDAO.trouverPhotoPrincipale(demandeur.getId());
                
                if (photo != null) {
                    info.setCheminPhoto(photo.getChemin());
                }
                
                com.hotia.model.Profil profil = profilDAO.trouverParUtilisateurId(demandeur.getId());
                
                if (profil != null && profil.getVilleId() != null) {
                    com.hotia.model.Ville ville = villeDAO.trouverParID(profil.getVilleId());
                
                    if (ville != null) {
                        info.setNomVille(ville.getNom());
                    }
                }
            }
            resultat.add(info);
        }
        return resultat;
    }
}