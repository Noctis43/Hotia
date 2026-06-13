package com.hotia.servlet;

import com.hotia.dao.UtilisateurDAO;
import com.hotia.dao.VilleDAO;
import com.hotia.model.Utilisateur;
import com.hotia.service.OnboardingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/onboarding")
@MultipartConfig(maxFileSize = 5242880, maxRequestSize = -1)
public class OnboardingServlet extends HttpServlet {

    private final OnboardingService onboardingService = new OnboardingService();
    private final VilleDAO villeDAO = new VilleDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        }
        
        try {
            Utilisateur u = ServletUtil.getUtilisateurConnecte(req);
        
            if (u.getProfileComplet() == 1) {
                resp.sendRedirect(req.getContextPath() + "/decouvrir");
                return;
            }
        
            chargerDonneesFormulaire(req);
            req.getRequestDispatcher("/jsp/onboarding/onboarding.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        }
        
        Utilisateur utilisateur = ServletUtil.getUtilisateurConnecte(req);

        try {
            req.setCharacterEncoding("UTF-8");
            String cheminPhoto = sauvegarderPhoto(req, utilisateur.getId());
            List<String> erreurs = onboardingService.enregistrerOnboarding(
                    utilisateur,
                    req.getParameter("telephone"),
                    req.getParameter("bio"),
                    ServletUtil.parseIntegerNullable(req.getParameter("villeId")),
                    req.getParameter("latitude"),
                    req.getParameter("longitude"),
                    req.getParameter("sexeCibleRecherche"),
                    req.getParameter("situationRecherche"),
                    req.getParameter("typeRelationRecherche"),
                    ServletUtil.parseIntegerNullable(req.getParameter("ageMinRecherche")),
                    ServletUtil.parseIntegerNullable(req.getParameter("ageMaxRecherche")),
                    ServletUtil.parseIntArray(req.getParameterValues("divertissementsRecherche")),
                    ServletUtil.parseIntArray(req.getParameterValues("comportementsRecherche")),
                    req.getParameter("sexeCibleDescription"),
                    req.getParameter("situationDescription"),
                    req.getParameter("typeRelationDescription"),
                    ServletUtil.parseIntegerNullable(req.getParameter("ageMinDescription")),
                    ServletUtil.parseIntegerNullable(req.getParameter("ageMaxDescription")),
                    ServletUtil.parseIntArray(req.getParameterValues("divertissementsDescription")),
                    ServletUtil.parseIntArray(req.getParameterValues("comportementsDescription")),
                    cheminPhoto);

            if (erreurs.isEmpty()) {
                Utilisateur misAJour = utilisateurDAO.trouverParId(utilisateur.getId());
                req.getSession().setAttribute("utilisateurConnecte", misAJour);
                resp.sendRedirect(req.getContextPath() + "/decouvrir");
            } else {
                req.setAttribute("erreurs", erreurs);
                chargerDonneesFormulaire(req);
                req.getRequestDispatcher("/jsp/onboarding/onboarding.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }

    private void chargerDonneesFormulaire(HttpServletRequest req) throws SQLException {
        
        req.setAttribute("villes", villeDAO.trouverToutes());
        Map<Integer, String> divertissements = onboardingService.chargerDivertissementsRef();
        Map<Integer, String> comportements = onboardingService.chargerComportementsRef();
        req.setAttribute("divertissementsRef", divertissements);
        req.setAttribute("comportementsRef", comportements);
    }

    private String sauvegarderPhoto(HttpServletRequest req, int utilisateurId) throws IOException, ServletException {
        
        Part part = req.getPart("photo");
        
        if (part == null || part.getSize() == 0) {
            return null;
        }
        
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String extension = "";
        int dot = fileName.lastIndexOf('.');
        
        if (dot >= 0) {
            extension = fileName.substring(dot);
        }
        
        String uniqueName = UUID.randomUUID().toString() + extension;
        String relativePath = "uploads/photos/" + uniqueName;

        String uploadDir = getServletContext().getRealPath("/") + "uploads" + File.separator + "photos";
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        part.write(dir.resolve(uniqueName).toString());

        return relativePath;
    }
}