package com.hotia.servlet;

import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Utilisateur;
import com.hotia.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/demandes")
public class DemandeMatchServlet extends HttpServlet {

    private final MatchService matchService = new MatchService();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        } 

        Utilisateur utilisateur = ServletUtil.getUtilisateurConnecte(req);

        if (utilisateur.getProfileComplet() != 1) {
            resp.sendRedirect(req.getContextPath() + "/onboarding");
            return;
        }

        try {
            List<MatchService.DemandeAvecInfos> demandes = matchService.listerDemandesRecues(utilisateur.getId());
            req.setAttribute("demandes", demandes);
            req.setAttribute("statutUtilisateur", utilisateur.getStatut());
            req.getRequestDispatcher("/jsp/match/demandes.jsp").forward(req, resp);
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
        String action = req.getParameter("action");
        int demandeId = Integer.parseInt(req.getParameter("demandeId"));

        try {
            if ("accepter".equals(action)) {
                String erreur = matchService.accepterDemande(demandeId, utilisateur.getId());

                if (erreur == null) {
                    Utilisateur misAJour = utilisateurDAO.trouverParId(utilisateur.getId());
                    req.getSession().setAttribute("utilisateurConnecte", misAJour);
                } else {
                    req.getSession().setAttribute("flashErreur", erreur);
                }
            } else if ("refuser".equals(action)) {
                matchService.refuserDemande(demandeId, utilisateur.getId());
            }

            resp.sendRedirect(req.getContextPath() + "/demandes");
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}