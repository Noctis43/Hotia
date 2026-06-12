package com.hotia.service;

import com.hotia.dao.PhotoDAO;
import com.hotia.dao.ProfilDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.dao.VilleDAO;
import com.hotia.model.Photo;
import com.hotia.model.Profil;
import com.hotia.model.Utilisateur;
import com.hotia.model.Ville;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DecouvrirService {

    /** Vue d'un candidat affiché sur la carte. */
    public static class CandidatCarte {
        
        private Utilisateur utilisateur;
        private Photo photoPrincipale;
        private String nomVille;
        private int age;
        private double score;

        public Utilisateur getUtilisateur() {
            return utilisateur;
        }

        public void setUtilisateur(Utilisateur utilisateur) {
            this.utilisateur = utilisateur;
        }

        public Photo getPhotoPrincipale() {
            return photoPrincipale;
        }

        public void setPhotoPrincipale(Photo photoPrincipale) {
            this.photoPrincipale = photoPrincipale;
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

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final PhotoDAO photoDAO = new PhotoDAO();
    private final ProfilDAO profilDAO = new ProfilDAO();
    private final VilleDAO villeDAO = new VilleDAO();
    private final CompatibiliteService compatibiliteService = new CompatibiliteService();
    private final AuthService authService = new AuthService();

    
    public CandidatCarte trouverProchainCandidat(int utilisateurCourantId, Set<Integer> cartesVues) throws SQLException {
        
        List<CandidatCarte> candidats = construireFeedTrie(utilisateurCourantId);
        
        if (cartesVues == null) {
            cartesVues = new HashSet<>();
        }
        
        for (CandidatCarte c : candidats) {
            if (!cartesVues.contains(c.getUtilisateur().getId())) {
                return c;
            }
        }
        return null;
    }

    public List<CandidatCarte> construireFeedTrie(int utilisateurCourantId) throws SQLException {
        
        List<Utilisateur> candidats = utilisateurDAO.trouverCandidatsDecouvrir(utilisateurCourantId);
        List<CandidatCarte> cartes = new ArrayList<>();

        for (Utilisateur u : candidats) {
            CandidatCarte carte = new CandidatCarte();
            carte.setUtilisateur(u);
            carte.setPhotoPrincipale(photoDAO.trouverPhotoPrincipale(u.getId()));
            carte.setAge(authService.calculerAge(u.getDateNaissance()));
            carte.setScore(compatibiliteService.calculerScore(utilisateurCourantId, u.getId()));

            Profil profil = profilDAO.trouverParUtilisateurId(u.getId());
        
            if (profil != null && profil.getVilleId() != null) {
                Ville ville = villeDAO.trouverParID(profil.getVilleId());
                if (ville != null) {
                    carte.setNomVille(ville.getNom());
                }
            }
        
            if (carte.getNomVille() == null) {
                carte.setNomVille("Madagascar");
            }
            cartes.add(carte);
        }

        cartes.sort(Comparator.comparingDouble(CandidatCarte::getScore).reversed());
        return cartes;
    }

    public void enregistrerPasse(int utilisateurId, int cibleId) throws SQLException {
        utilisateurDAO.enregistrerSwipePasse(utilisateurId, cibleId);
    }
}
