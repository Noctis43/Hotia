package com.hotia.service;

import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Utilisateur;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class AuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    /** Inscrit un nouvel utilisateur. */
    public List<String> inscrire(String nom, String prenom, String email, String dateNaissanceStr, String sexe, String motDePasse, String confirmerMotDePasse) throws SQLException {
        
        List<String> erreurs = new ArrayList<>();

        if (nom == null || nom.trim().isEmpty()) {
            erreurs.add("Le nom est obligatoire.");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            erreurs.add("Le prénom est obligatoire.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            erreurs.add("L'email est invalide.");
        }
        if (sexe == null || sexe.trim().isEmpty()) {
            erreurs.add("Le sexe est obligatoire.");
        }
        if (motDePasse == null || motDePasse.length() < 8) {
            erreurs.add("Le mot de passe doit contenir au moins 8 caractères.");
        }
        if (confirmerMotDePasse == null || !confirmerMotDePasse.equals(motDePasse)) {
            erreurs.add("Les mots de passe ne correspondent pas.");
        }

        LocalDate dateNaissance = null;
        
        try {
            dateNaissance = LocalDate.parse(dateNaissanceStr);
            int age = calculerAge(dateNaissance);
        
            if (age < 18) {
                erreurs.add("Vous devez avoir au moins 18 ans pour vous inscrire.");
            }
        } catch (Exception e) {
            erreurs.add("La date de naissance est invalide.");
        }

        if (!erreurs.isEmpty()) {
            return erreurs;
        }

        if (utilisateurDAO.trouverParEmail(email.trim()) != null) {
            erreurs.add("Cet email est déjà utilisé.");
            return erreurs;
        }

        Utilisateur u = new Utilisateur();
        u.setNom(nom.trim());
        u.setPrenom(prenom.trim());
        u.setEmail(email.trim());
        u.setDateNaissance(dateNaissance);
        u.setSexe(sexe);
        u.setMotDePasse(hasherMotDePasse(motDePasse));
        u.setStatut("libre");
        u.setProfileComplet(0);
        utilisateurDAO.creerUtilisateur(u);
       
        return erreurs;
    }

    /** Authentifie un utilisateur par email et mot de passe en clair. */
    public Utilisateur connecter(String email, String motDePasse) throws SQLException {
        
        if (email == null || motDePasse == null) {
            return null;
        }
        
        String hash = hasherMotDePasse(motDePasse);
        return utilisateurDAO.trouverParEmailEtMotDePasse(email.trim(), hash);
    }

    /** Calcule l'âge à partir d'une date de naissance. */
    public int calculerAge(LocalDate dateNaissance) {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    /** Hache un mot de passe en SHA-256 hexadécimal. */
    public String hasherMotDePasse(String motDePasse) {
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
        
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
        
            return sb.toString();
        
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
    }
}
