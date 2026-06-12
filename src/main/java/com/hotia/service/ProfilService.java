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
     
}