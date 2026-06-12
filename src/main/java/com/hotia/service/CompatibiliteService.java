package com.hotia.service;

import com.hotia.dao.PreferenceDAO;
import com.hotia.dao.ProfilDAO;
import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Preference;
import com.hotia.model.Profil;
import com.hotia.model.Utilisateur;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class CompatibiliteService {

    private static final double RAYON_TERRE_KM = 6371.0;
    private static final double DISTANCE_MAX_KM = 200.0;

    private final PreferenceDAO preferenceDAO = new PreferenceDAO();
    private final ProfilDAO profilDAO = new ProfilDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private final AuthService authService = new AuthService();

    public double calculerScore(int utilisateurAId, int utilisateurBId) throws SQLException {

        Profil profilA = profilDAO.trouverParUtilisateurId(utilisateurAId);
        Profil profilB = profilDAO.trouverParUtilisateurId(utilisateurBId);

        if (profilA ==null || profilB == null || profilA.getLatitude() == null || profilA.getLongitude() == null || profilB.getLatitude() == null || profilB.getLongitude() == null) {
            return 0.0;
        }

        double distance = calculerDistanceKm(profilA.getLatitude().doubleValue(), profilA.getLongitude().doubleValue(), profilB.getLatitude().doubleValue(), profilB.getLongitude().doubleValue());
        double scoreDistance = Math.max(0.0, ((DISTANCE_MAX_KM - distance) / DISTANCE_MAX_KM) * 100.0);
        double scorePreferences = calculerScorePreferences(utilisateurAId, utilisateurBId);

        return (0.40 * scoreDistance) + (0.60 * scorePreferences);
    }

    public double calculerDistanceKm(double lat1, double lon1, double lat2, double lon2) {

        double dlat = Math.toRadians(lat2 - lat1);
        double dlon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 - Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RAYON_TERRE_KM * c;
    }

    public double calculerScorePreferences(int utilisateurAId, int utilisateurBId) throws SQLException {

        Preference rechercheA = preferenceDAO.trouverParUtilisateurEtType(utilisateurAId, "recherche");
        Preference descriptionB = preferenceDAO.trouverParUtilisateurEtType(utilisateurBId, "description");
        Utilisateur utilisateurB = utilisateurDAO.trouverParId(utilisateurBId);

        if (rechercheA == null || descriptionB == null || utilisateurB == null) {
            return 0.0;
        }

        int criteresValides = 0;

        if (critereSexeValide(rechercheA.getSexeCible(), utilisateurB.getSexe())) {
            criteresValides++;
        }
        
        if (critereComportementsValide(rechercheA.getComportements(), descriptionB.getComportements())) {
            criteresValides++;
        }
        
        if (critereSituationValide(rechercheA.getSituationFamiliale(), descriptionB.getSituationFamiliale())) {
            criteresValides++;
        }
        
        if (critereTypeRelationValide(rechercheA.getTypeRelation(), descriptionB.getTypeRelation())) {
            criteresValides++;
        }
        
        if (critereAgeValide(rechercheA.getAgeMin(), rechercheA.getAgeMax(), utilisateurB.getDateNaissance())) {
            criteresValides++;
        }
        
        if (critereDivertissementsValide(rechercheA.getDivertissements(), descriptionB.getDivertissements())) {
            criteresValides++;
        }

        return (criteresValides / 6.0) * 100.0;
    }

    private boolean critereSexeValide(String sexeRecherche, String sexeUtilisateur) {
        
        if (sexeRecherche == null || "Peu importe".equals(sexeRecherche)) {
            return true;
        }
        
        return sexeRecherche.equals(sexeUtilisateur);
    }

    private boolean critereComportementsValide(List<String> recherches, List<String> declares) {
        
        if (recherches == null || recherches.isEmpty()) {
            return true;
        }
        
        if (declares == null || declares.isEmpty()) {
            return false;
        }
        
        for (String r : recherches) {
            if (declares.contains(r)) {
                return true;
            }
        }
        
        return false;
    }

    private boolean critereSituationValide(String situationAcceptee, String situationB) {
     
        if (situationAcceptee == null || "Peu importe".equals(situationAcceptee)) {
            return true;
        }
     
        return situationAcceptee.equals(situationB);
    }

    private boolean critereTypeRelationValide(String typeRecherche, String typeB) {
        
        if (typeRecherche == null || "Peu importe".equals(typeRecherche)) {
            return true;
        }
        
        return typeRecherche.equals(typeB);
    }

    private boolean critereAgeValide(Integer ageMin, Integer ageMax, java.time.LocalDate dateNaissanceB) {
       
        if (ageMin == null && ageMax == null) {
            return true;
        }
       
        int ageB = authService.calculerAge(dateNaissanceB);
       
        if (ageMin != null && ageB < ageMin) {
            return false;
        }
       
        if (ageMax != null && ageB > ageMax) {
            return false;
        }
       
        return true;
    }

    private boolean critereDivertissementsValide(List<String> recherches, List<String> pratiques) {
        
        if (recherches == null || recherches.isEmpty()) {
            return true;
        }
        
        if (pratiques == null || pratiques.isEmpty()) {
            return false;
        }
        
        for (String r : recherches) {
            if (pratiques.contains(r)) {
                return true;
            }
        }
        
        return false;
    }
}