package com.hotia.model;

import java.math.BigDecimal;

public class Profil {

    private int id;
    private int utilisateurId;
    private String telephone;
    private String bio;
    private Integer villeId;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Profil() {}

    public Profil(int id, int utilisateurId, String telephone, String bio, Integer villeId, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.telephone = telephone;
        this.bio = bio;
        this.villeId = villeId;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getVilleId() {
        return villeId;
    }

    public void setVilleId(Integer villeId) {
        this.villeId = villeId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}