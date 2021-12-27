package com.example.CrazyRoadGame.model.records;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Record {
    private String playerName = "";
    private int score = 0;
    private Date date;
    private double lat = 0.0;
    private double lon = 0.0;



    public Record() { }

    public Record setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public Record setTime() {
        date = new Date();
         return this;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public Record setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        return df.format(date);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

