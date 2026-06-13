package com.hotia.servlet;

import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Separation;
import com.hotia.model.Utilisateur;
import com.hotia.service.SeparationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/separation")
public class SeparationServlet extends HttpServlet {

    private final SeparationService separationService = new SeparationService();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        }
        
        Utilisateur utilisateur = ServletUtil.getUtilisateurConnecte(req);
        
        if (!"en_matching".equals(utilisateur.getStatut())) {
            resp.sendRedirect(req.getContextPath() + "/decouvrir");
            return;
        }

        try {
            Separation separation = separationService.trouverSeparationEnAttente(utilisateur.getId());
            req.setAttribute("separation", separation);
            req.setAttribute("prenomPartenaire", separationService.trouverPrenomPartenaire(utilisateur.getId()));
            req.getRequestDispatcher("/jsp/match/separation.jsp").forward(req, resp);
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
        
        if (!"en_matching".equals(utilisateur.getStatut())) {
            resp.sendRedirect(req.getContextPath() + "/decouvrir");
            return;
        }

        String action = req.getParameter("action");
        
        try {
            if ("demander".equals(action)) {
                String erreur = separationService.demanderSeparation(utilisateur.getId());
            
                if (erreur != null) {
                    req.getSession().setAttribute("flashErreur", erreur);
                }
            } else if ("confirmer".equals(action)) {
                int separationId = Integer.parseInt(req.getParameter("separationId"));
                String erreur = separationService.confirmerSeparation(separationId, utilisateur.getId());
            
                if (erreur == null) {
                    Utilisateur misAJour = utilisateurDAO.trouverParId(utilisateur.getId());
                    req.getSession().setAttribute("utilisateurConnecte", misAJour);
                } else {
                    req.getSession().setAttribute("flashErreur", erreur);
                }
            } else if ("refuser".equals(action)) {
                int separationId = Integer.parseInt(req.getParameter("separationId"));
                separationService.refuserSeparation(separationId, utilisateur.getId());
            }
            
            resp.sendRedirect(req.getContextPath() + "/separation");
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}