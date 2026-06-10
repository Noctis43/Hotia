package com.hotia.model;

import java.time.LocalDateTime;

public class Message {

    private int id;
    private int matchId;
    private int emetteurId;
    private String contenu;
    private LocalDateTime dateEnvoi;

    public Message() {}

    public Message(int id, int matchId, int emetteurId, String contenu, LocalDateTime dateEnvoi) {
        this.id = id;
        this.matchId = matchId;
        this.emetteurId = emetteurId;
        this.contenu = contenu;
        this.dateEnvoi = dateEnvoi;
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

    public int getEmetteurId() {
        return emetteurId;
    }

    public void setEmetteurId(int emetteurId) {
        this.emetteurId = emetteurId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
