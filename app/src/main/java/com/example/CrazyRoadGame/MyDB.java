package com.example.CrazyRoadGame;

import java.util.ArrayList;

public class MyDB {
    private ArrayList<Record> records = new ArrayList<>();

    public MyDB() {    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public MyDB setRecords(ArrayList<Record> records){
        this.records = records;
        return this;
    }
}
