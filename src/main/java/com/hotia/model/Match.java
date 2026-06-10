package com.hotia.model;

import java.time.LocalDateTime;

public class Match {

    private int id;
    private int utilisateur1Id;
    private int utilisateur2Id;
    private int actif;
    private LocalDateTime dateMatch;

    public Match() {}

    public Match(int id, int utilisateur1Id, int utilisateur2Id, int actif, LocalDateTime dateMatch) {
        this.id = id;
        this.utilisateur1Id = utilisateur1Id;
        this.utilisateur2Id = utilisateur2Id;
        this.actif = actif;
        this.dateMatch = dateMatch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateur1Id() {
        return utilisateur1Id;
    }

    public void setUtilisateur1Id(int utilisateur1Id) {
        this.utilisateur1Id = utilisateur1Id;
    }

    public int getUtilisateur2Id() {
        return utilisateur2Id;
    }

    public void setUtilisateur2Id(int utilisateur2Id) {
        this.utilisateur2Id = utilisateur2Id;
    }

    public int getActif() {
        return actif;
    }

    public void setActif(int actif) {
        this.actif = actif;
    }

    public LocalDateTime getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(LocalDateTime dateMatch) {
        this.dateMatch = dateMatch;
    }
}