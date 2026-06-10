package com.hotia.model;

import java.time.LocalDateTime;

public class DemandeMatch {

    private int id;
    private int demandeurId;
    private int cibleId;
    private String statut;
    private LocalDateTime dateDemande;

    public DemandeMatch() {}

     public DemandeMatch(int id, int demandeurId, int cibleId, String statut, LocalDateTime dateDemande) {
        this.id = id;
        this.demandeurId = demandeurId;
        this.cibleId = cibleId;
        this.statut = statut;
        this.dateDemande = dateDemande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemandeurId() {
        return demandeurId;
    }

    public void setDemandeurId(int demandeurId) {
        this.demandeurId = demandeurId;
    }

    public int getCibleId() {
        return cibleId;
    }

    public void setCibleId(int cibleId) {
        this.cibleId = cibleId;
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