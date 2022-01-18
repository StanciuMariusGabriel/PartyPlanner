package com.example.dam.data.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Party {
    private int id;
    private int idUserPlanner;
    private String partyName;
    @NotNull
    private String partyLocation;
    @NotNull
    private String partyDate;

    // Constructor
    public Party(int id, String partyName, @NonNull String partyLocation, @NonNull String partyDate, int idUserPlanner ) {
        this.id = id;
        this.partyName = partyName;
        this.partyLocation = partyLocation;
        this.partyDate = partyDate;
        this.idUserPlanner = idUserPlanner;
    }

    public Party(String partyName, @NonNull String partyLocation, @NonNull String partyDate, int idUserPlanner ) {
        this.partyName = partyName;
        this.partyLocation = partyLocation;
        this.partyDate = partyDate;
        this.idUserPlanner = idUserPlanner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUserPlanner() {
        return idUserPlanner;
    }

    public void setIdUserPlanner(int idUserPlanner) {
        this.idUserPlanner = idUserPlanner;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLocation() {
        return partyLocation;
    }

    public void setPartyLocation(String partyLocation) {
        this.partyLocation = partyLocation;
    }

    public String getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(String partyDate) {
        this.partyDate = partyDate;
    }

}