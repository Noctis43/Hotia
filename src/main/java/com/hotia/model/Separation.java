package com.hotia.model;

import java.time.LocalDateTime;

public class Separation {

    private int id;
    private int matchId;
    private int demandeurId;
    private String statut;
    private LocalDateTime dateDemande;

    public Separation() {}

    public Separation(int id, int matchId, int demandeurId, String statut, LocalDateTime dateDemande) {
        this.id = id;
        this.matchId = matchId;
        this.demandeurId = demandeurId;
        this.statut = statut;
        this.dateDemande = dateDemande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getDemandeurId() {
        return demandeurId;
    }

    public void setDemandeurId(int demandeurId) {
        this.demandeurId = demandeurId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }
} 