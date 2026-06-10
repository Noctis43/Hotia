package com.hotia.model;

import java.util.ArrayList;
import java.util.List;

public class Preference {

    private int id;
    private int utilisateurId;
    private String type;
    private String sexeCible;
    private String situationFamiliale;
    private String typeRelation;
    private Integer ageMin;
    private Integer ageMax;
    private Liste<String> divertissements;
    private Liste<String> comportements;

    public Preference() {
        this.divertissements = new ArrayList<>();
        this.comportements = new ArrayList<>();
    }

    public Preference(int id, int utilisateurId, String type, String sexeCible,
                      String situationFamiliale, String typeRelation, Integer ageMin, Integer ageMax,
                      List<String> divertissements, List<String> comportements) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.type = type;
        this.sexeCible = sexeCible;
        this.situationFamiliale = situationFamiliale;
        this.typeRelation = typeRelation;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.divertissements = divertissements != null ? divertissements : new ArrayList<>();
        this.comportements = comportements != null ? comportements : new ArrayList<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSexeCible() {
        return sexeCible;
    }

    public void setSexeCible(String sexeCible) {
        this.sexeCible = sexeCible;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public String getTypeRelation() {
        return typeRelation;
    }

    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

    public Integer getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin) {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax) {
        this.ageMax = ageMax;
    }

    public List<String> getDivertissements() {
        return divertissements;
    }

    public void setDivertissements(List<String> divertissements) {
        this.divertissements = divertissements;
    }

    public List<String> getComportements() {
        return comportements;
    }

    public void setComportements(List<String> comportements) {
        this.comportements = comportements;
    }
}