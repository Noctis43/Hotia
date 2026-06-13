package com.hotia.servlet;

import com.hotia.model.Utilisateur;
import com.hotia.service.ProfilService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/profil-detail")
public class ProfilDetailServlet extends HttpServlet {

    private final ProfilService profilService = new ProfilService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        if (ServletUtil.redirigerSiNonConnecte(req, resp)) {
            return;
        }
        
        Utilisateur connecte = ServletUtil.getUtilisateurConnecte(req);
        
        if (connecte.getProfileComplet() != 1) {
            resp.sendRedirect(req.getContextPath() + "/onboarding");
            return;
        }

        int profilId = Integer.parseInt(req.getParameter("id"));
        
        try {
            Map<String, Object> profil = profilService.getProfilComplet(profilId, connecte.getId());
            req.setAttribute("profilData", profil);
            req.getRequestDispatcher("/jsp/decouvrir/profil_detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}