package com.hotia.servlet;

import com.hotia.dao.UtilisateurDAO;
import com.hotia.model.Message;
import com.hotia.model.Utilisateur;
import com.hotia.service.MessageService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {

    private final MessageService messageService = new MessageService();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (ServletUtil.redirigereSiNonConnect(req, resp)) {
            return;
        }

        Utilisateur utilisateur = ServletUtil.getUtilisateurConnecte(req);

        if (!"en_matching".equals(utilisateur.getStatut())) {
            resp.sendRedirect(req.getContextPath() + "/decouvrir");
            return;
        }

        try {
        	List<Message> messages = messageService.chargerHistorique(utilisateur.getId());
            int partenaireId = messageService.trouverPartenaireId(utilisateur.getId());
            Utilisateur partenaire = utilisateurDAO.trouverParId(partenaireId);
            req.setAttribute("messages", messages);
            req.setAttribute("partenaire", partenaire);
            req.setAttribute("utilisateurConnecte", utilisateur);
            req.getRequestDispatcher("/jsp/messagerie/chat.jsp").forward(req, resp);
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
        String contenu = req.getParameter("contenu");

        try {
            String erreur = messageService.envoyerMessage(utilisateur.getId(), contenu);
            
            if (erreur != null) {
                req.getSession().setAttribute("flashErreur", erreur);
            }
            resp.sendRedirect(req.getContextPath() + "/chat");
        } catch (SQLException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/jsp/commun/erreur.jsp").forward(req, resp);
        }
    }
}