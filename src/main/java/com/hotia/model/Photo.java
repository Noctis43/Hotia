package com.hotia.model;

import java.time.LocalDateTime;

public class Photo {

    private int id;
    private int utilisateurId;
    private String chemin;
    private int estPrincipale;
    private LocalDateTime dateUpload;

    public Photo() {}

    public Photo(int id, int utilisateurId, String chemin, int estPrincipale, LocalDateTime dateUpload) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.chemin = chemin;
        this.estPrincipale = estPrincipale;
        this.dateUpload = dateUpload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public int getEstPrincipale() {
        return estPrincipale;
    }

    public void setEstPrincipale(int estPrincipale) {
        this.estPrincipale = estPrincipale;
    }

    public LocalDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }
}