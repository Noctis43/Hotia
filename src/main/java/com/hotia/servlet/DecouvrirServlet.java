package com.hotia.servlet;

import com.hotia.model.Utilisateur;
import com.hotia.service.DecouvrirService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/decouvrir")
public class DecouvrirServlet extends HttpServlet {

    private final DecouvrirService decouvrirService = new DecouvrirService();

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
            if ("en_matching".equals(utilisateur.getStatut())) {
                req.setAttribute("enMatching", true);
            } else {
                HttpSession session = req.getSession();
                Set<Integer> cartesVues = ServletUtil.getCartesVues(session);
                DecouvrirService.CandidatCarte candidat = decouvrirService.trouverProchainCandidat(utilisateur.getId(), cartesVues);
                req.setAttribute("candidat", candidat);
            }
            req.getRequestDispatcher("jsp/decouvrir/decouvrir.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}