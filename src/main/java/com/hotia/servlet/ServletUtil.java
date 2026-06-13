package com.hotia.servlet;

import com.hotia.model.Utilisateur;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class ServletUtil {

    private ServletUtil() {}

    public static Utilisateur getUtilisateurConnecte(HttpServletRequest req) {
        
        HttpSession session = req.getSession(false);
        
        if (session == null) {
            return null;
        }
        
        Object attr = session.getAttribute("utilisateurConnecte");
        
        if (attr instanceof Utilisateur) {
            return (Utilisateur) attr;
        }
        return null;
    }

    public static boolean redirigerSiNonConnecte(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        if (getUtilisateurConnecte(req) == null) {
            resp.sendRedirect(req.getContextPath() + "/connexion");
            return true;
        }
        
        return false;
    }
    
    public static int[] parseIntArray(String[] values) {
        
        if (values == null || values.length == 0) {
            return new int[0];
        }
        
        int[] result = new int[values.length];
        
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && !values[i].trim().isEmpty()) {
                result[i] = Integer.parseInt(values[i].trim());
            }
        }
        return result;
    }

    public static Integer parseIntegerNullable(String value) {
       
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
       
        return Integer.parseInt(value.trim());
    }

    @SuppressWarnings("unchecked")
    public static Set<Integer> getCartesVues(HttpSession session) {
        
        Object attr = session.getAttribute("cartesVues");
        
        if (attr instanceof Set) {
            return (Set<Integer>) attr;
        }
        
        Set<Integer> set = new HashSet<>();
        session.setAttribute("cartesVues", set);
        return set;
    }
}