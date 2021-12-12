package com.example.CrazyRoadGame;

import java.time.LocalDate;


public class Record {
    private String playerName = "";
    private int score = 0;
    private LocalDate date;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public Record() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public Record setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }


    public LocalDate getTime() {
        return date;
    }

    public Record setTime(LocalDate time) {
        this.date = time;
         return this;
    }


    public double getLat() {
        return latitude;
    }

    public Record setLat(double lat) {
        this.latitude = lat;
        return this;
    }

    public double getLon() {
        return longitude;
    }

    public Record setLon(double lon) {
        this.longitude = lon;
        return this;
    }
}

