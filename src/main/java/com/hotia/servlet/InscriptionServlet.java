package com.hotia.servlet;

import com.hotia.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         req.getRequestDispatcher("/jsp/auth/inscription.jsp").forward(req, resp);
    }

     @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String dateNaissance = req.getParameter("dateNaissance");
        String sexe = req.getParameter("sexe");
        String motDePasse = req.getParameter("motDePasse");
        String confirmerMotDePasse = req.getParameter("confirmerMotDePasse");

        try {
            List<String> erreurs = authService.inscrire(nom, prenom, email, dateNaissance, sexe,
                    motDePasse, confirmerMotDePasse);
            
            if (erreurs.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/connexion");
            } else {
                req.setAttribute("erreurs", erreurs);
                req.setAttribute("nom", nom);
                req.setAttribute("prenom", prenom);
                req.setAttribute("email", email);
                req.setAttribute("dateNaissance", dateNaissance);
                req.setAttribute("sexe", sexe);
                req.getRequestDispatcher("/jsp/auth/inscription.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            req.setAttribute("erreur", "Erreur serveur : " + e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}