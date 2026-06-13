package com.hotia.servlet;

import com.hotia.model.Utilisateur;
import com.hotia.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/connexion")
public class ConnexionServelet extends HttpServlet {

    private final AuthService authService = new AuthService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/auth/connexion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String motDePasse = req.getParameter("motDePasse");

        try {
            Utilisateur utilisateur = authService.connecter(email, motDePasse);

            if (utilisateur != null) {
                req.getSession().setAttribute("utilisateurConnecte", utilisateur);

                if (utilisateur.getProfileComplet() == 0) {
                    resp.sendRedirect(req.getContextPath() + "/onboarding");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/decouvrir");
                }
            } else {
                req.setAttribute("erreur", "Email ou mot de passe incorrect.");
                req.getRequestDispatcher("/jsp/auth/connexion.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("erreur", "erreur serveur : " + e.getMessage());
            req.getRequestDispatcher("jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}