package com.hotia.servlet;

import com.hotia.model.Utilisateur;
import com.hotia.service.DecouvrirService;
import com.hotia.service.MatchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/swipe")
public class SwipeServlet extends HttpServlet {

    private final MatchService matchService = new MatchService();
    private final DecouvrirService decouvrirService = new DecouvrirService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        }

        Utilisateur utilisateur = ServletUtil.getUtilisateurConnecte(req);
        String action = req.getParameter("action");
        int cibleId = Integer.parseInt(req.getParameter("cibleId"));

        HttpSession session = req.getSession();
        Set<Integer> cartesVues = ServletUtil.getCartesVues(session);
        cartesVues.add(cibleId);

        try {
            if ("gauche".equals(action)) {
                matchService.envoyerDemande(utilisateur.getId(), cibleId);
            } else if ("droite".equals(action)) {
                decouvrirService.enregistrerPasse(utilisateur.getId(), cibleId);
            }
            resp.sendRedirect(req.getContextPath() + "/decouvrir");
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}