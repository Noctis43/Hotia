package com.hotia.service;

import com.hotia.dao.PhotoDAO;
import com.hotia.dao.PreferenceDAO;
import com.hotia.dao.ProfilDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.dao.VilleDAO;
import com.hotia.model.Photo;
import com.hotia.model.Preference;
import com.hotia.model.Profil;
import com.hotia.model.Utilisateur;
import com.hotia.model.Ville;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilService {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final ProfilDAO profilDAO = new ProfilDAO();
    private final PhotoDAO photoDAO = new PhotoDAO();
    private final PreferenceDAO preferenceDAO = new PreferenceDAO();
    private final VilleDAO villeDAO = new VilleDAO();
    private final OnboardingService onboardingService = new OnboardingService();
    private final CompatibiliteService compatibiliteService = new CompatibiliteService();
    private final AuthService authService = new AuthService();

    
    public Map<String, Object> getProfilComplet(int utilisateurId, int utilisateurConnecteId) throws SQLException {
        
        Map<String, Object> data = new HashMap<>();
        Utilisateur utilisateur = utilisateurDAO.trouverParId(utilisateurId);
        Profil profil = profilDAO.trouverParUtilisateurId(utilisateurId);
        List<Photo> photos = photoDAO.trouverParUtilisateurId(utilisateurId);
        Preference recherche = preferenceDAO.trouverParUtilisateurEtType(utilisateurId, "recherche");
        Preference description = preferenceDAO.trouverParUtilisateurEtType(utilisateurId, "description");

        data.put("utilisateur", utilisateur);
        data.put("profil", profil);
        data.put("photos", photos);
        data.put("preferenceRecherche", recherche);
        data.put("preferenceDescription", description);

        if (utilisateur != null) {
            data.put("age", authService.calculerAge(utilisateur.getDateNaissance()));
        }

        String nomVille = null;
        
        if (profil != null && profil.getVilleId() != null) {
            Ville ville = villeDAO.trouverParID(profil.getVilleId());
            
            if (ville != null) {
                nomVille = ville.getNom();
            }
        }
        
        data.put("nomVille", nomVille);

        if (utilisateurConnecteId > 0 && utilisateurConnecteId != utilisateurId) {
            data.put("scoreCompatibilite", compatibiliteService.calculerScore(utilisateurConnecteId, utilisateurId));
        }

        return data;
    }

    public List<String> modifierProfil(Utilisateur utilisateur, String telephone, String bio,
                                        Integer villeId, String latitudeStr, String longitudeStr,
                                        String sexeCibleRecherche, String situationRecherche, String typeRelationRecherche,
                                        Integer ageMinRecherche, Integer ageMaxRecherche,
                                        int[] divertissementsRecherche, int[] comportementsRecherche,
                                        String sexeCibleDescription, String situationDescription, String typeRelationDescription,
                                        Integer ageMinDescription, Integer ageMaxDescription,
                                        int[] divertissementsDescription, int[] comportementsDescription,
                                        String cheminPhoto) throws SQLException {
        
        return onboardingService.enregistrerOnboarding(utilisateur, telephone, bio, villeId, latitudeStr, longitudeStr,
                sexeCibleRecherche, situationRecherche, typeRelationRecherche, ageMinRecherche, ageMaxRecherche,
                divertissementsRecherche, comportementsRecherche,
                sexeCibleDescription, situationDescription, typeRelationDescription,
                ageMinDescription, ageMaxDescription,
                divertissementsDescription, comportementsDescription, cheminPhoto);
    }

    public Map<String, Object> chargerProfilPourModification(int utilisateurId) throws SQLException {
        return getProfilComplet(utilisateurId, 0);
    } 
}