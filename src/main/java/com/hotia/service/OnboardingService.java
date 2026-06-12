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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class OnboardingService {

    private final Pattern TELEPHONE_PATTERN = Pattern.compile("^03[0-9][0-9]{2}[0-9]{3}[0-9]{2}$");
    private final ProfilDAO profilDAO = new ProfilDAO();
    private final PhotoDAO photoDAO = new PhotoDAO();
    private final VilleDAO villeDAO = new VilleDAO();
    private final PreferenceDAO preferenceDAO = new PreferenceDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<String> enregistrerOnboarding(Utilisateur utilisateur, String telephone, String bio,
                                                Integer villeId, String latitudeStr, String longitudeStr,
                                                String sexeCibleRecherche, String situationRecherche, String typeRelationRecherche,
                                                Integer ageMinRecherche, Integer ageMaxRecherche,
                                                int[] divertissementsRecherche, int[] comportementsRecherche,
                                                String sexeCibleDescription, String situationDescription, String typeRelationDescription,
                                                Integer ageMinDescription, Integer ageMaxDescription,
                                                int[] divertissementsDescription, int[] comportementsDescription,
                                                String cheminPhoto) throws SQLException {
            
        List<String> erreurs = new ArrayList<>();

        boolean photoObligatoire = (photoDAO.trouverPhotoPrincipale(utilisateur.getId()) == null);

        if (photoObligatoire && (cheminPhoto == null || cheminPhoto.isEmpty())) {
            erreurs.add("La photo de profile est obligatoire.");
        }

        if (telephone == null || !TELEPHONE_PATTERN.matcher(telephone.trim()).matches()) {
            erreurs.add("Le numero de téléphne doit respecter le format 03X XX XXX XX.");
        }

        if (bio != null && bio.length() > 500) {
            erreurs.add("La biographie ne peut pas dépasser 500 caractères.");
        }

        BigDecimal latitude = null;
        BigDecimal longitude = null;
        Integer villeIdFinal = villeId;

        if (villeId != null && villeId > 0) {
            Ville ville = villeDAO.trouverParID(villeId);

            if (ville != null) {
                latitude = ville.getLatitude();
                longitude = ville.getLongitude();
            }
        } else if (latitudeStr != null && longitudeStr != null && !latitudeStr.isEmpty() && !longitudeStr.isEmpty()) {

            try {
                latitude = new BigDecimal(latitudeStr);
                longitude = new BigDecimal(longitudeStr);
                villeIdFinal = null;
            } catch (NumberFormatException e) {
                erreurs.add("Les coordonnées GPS sont invalides.");
            }
        } else {
            erreurs.add("La localisation est obligatoire (Ville ou GPS). ");
        }

        if (!erreurs.isEmpty()) {
            return erreurs;
        }

        Profil profilExistant = profilDAO.trouverParUtilisateurId(utilisateur.getId());
        Profil profil = profilExistant != null ? profilExistant : new Profil();
        profil.setUtilisateurId(utilisateur.getId());
        profil.setTelephone(telephone.trim());
        profil.setBio(bio != null ? bio.trim() : null);
        profil.setVilleId(villeIdFinal);
        profil.setLatitude(latitude);
        profil.setLongitude(longitude);

        if (profilExistant == null) {
            profilDAO.creerProfil(profil);
        } else {
            profilDAO.mettreAJourProfil(profil);
        }

        if (cheminPhoto != null && !cheminPhoto.isEmpty()) {
            photoDAO.supprimerParUtilisateurId(utilisateur.getId());
            Photo photo = new Photo();
            photo.setUtilisateurId(utilisateur.getId());
            photo.setChemin(cheminPhoto);
            photo.setEstPrincipale(1);
            photoDAO.insererPhoto(photo);
        }

        enregistrerPreference(utilisateur.getId(), "recherche", sexeCibleRecherche, situationRecherche, typeRelationRecherche, ageMinRecherche, ageMaxRecherche, divertissementsRecherche, comportementsRecherche);

        enregistrerPreference(utilisateur.getId(), "description", sexeCibleDescription, situationDescription, typeRelationDescription, ageMinDescription, ageMaxDescription, divertissementsDescription, comportementsDescription);

        finaliserOnboarding(utilisateur.getId());
        
        return erreurs;
    }

    private void enregistrerPreference(int utilisateurId, String type, String sexeCible, String situation,
                                       String typeRelation, Integer ageMin, Integer ageMax,
                                       int[] divertissementIds, int[] comportementIds) throws SQLException {

        Preference pref = preferenceDAO.trouverParUtilisateurEtType(utilisateurId, type);

        if (pref == null) {
            pref = new Preference();
            pref = new Preference();
            pref.setUtilisateurId(utilisateurId);
            pref.setType(type);
            pref.setSexeCible(sexeCible);
            pref.setSituationFamiliale(situation);
            pref.setTypeRelation(typeRelation);
            pref.setAgeMin(ageMin);
            pref.setAgeMax(ageMax);
            int prefId = preferenceDAO.creerPreference(pref);
            pref.setId(prefId);
        } else {
             pref.setSexeCible(sexeCible);
            pref.setSituationFamiliale(situation);
            pref.setTypeRelation(typeRelation);
            pref.setAgeMin(ageMin);
            pref.setAgeMax(ageMax);
            preferenceDAO.mettreAJourPreference(pref);
            preferenceDAO.supprimerDivertissementsByPreferenceId(pref.getId());
            preferenceDAO.supprimerComportementsByPreferenceId(pref.getId());
        }

        if (divertissementIds != null) {
            for (int divId : divertissementIds) {
                preferenceDAO.insererDivertissement(pref.getId(), divId);
            }
        }

        if (comportementIds != null) {
            for (int compId : comportementIds) {
                preferenceDAO.insererComportement(pref.getId(), compId);
            }
        }
    }

    public void finaliserOnboarding(int utilisateurId) throws SQLException {
        Profil profil = profilDAO.trouverParUtilisateurId(utilisateurId);
        Photo photo = photoDAO.trouverPhotoPrincipale(utilisateurId);

        if (profil != null && photo != null && profil.getTelephone() != null && profil.getLatitude() != null && profil.getLongitude() != null) {
            utilisateurDAO.mettreAJourProfilComplet(utilisateurId, 1);
        }
    }

    public Map<Integer, String> chargerDivertissementsRef() throws SQLException {
        return preferenceDAO.trouverTousDivertissementsRef();
    }   

    public Map<Integer, String> chargerComportementsRef() throws SQLException {
        return preferenceDAO.trouverTousComportementsRef();
    } 
}