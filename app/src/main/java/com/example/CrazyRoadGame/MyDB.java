package com.example.CrazyRoadGame;


import com.example.CrazyRoadGame.model.records.Record;

import java.util.ArrayList;

public class MyDB {
    private ArrayList<Record> recordsList = new ArrayList<>(MAX_RECORDS);
    public static final int MAX_RECORDS = 10;
    private static MyDB myDB;

    private MyDB() {
    }


    public static MyDB getInstance() {
        return myDB;
    }

    public static MyDB init() {
        if (myDB == null) {
            myDB = new MyDB();
        }
        return myDB;
    }


    public ArrayList<Record> getAllRecords() {
        return recordsList;
    }

    public MyDB setRecordList(ArrayList<Record> records) {
        this.recordsList = records;
        return this;
    }

    public void setNewRecord(Record newRecord) {
        int index = 0;
        if (recordsList.isEmpty())
            recordsList.add(index, newRecord);
        else {
            for (int i = recordsList.size(); i > 0; i--) {
                if (newRecord.getScore() <= recordsList.get(i - 1).getScore()) {
                    index = i;
                    break;
                }
            }
            if (recordsList.size() >= MAX_RECORDS)
                recordsList.remove(MAX_RECORDS-1);
            recordsList.add(index, newRecord);
        }
    }

    public Record getLowestRecord() {
        if (recordsList.isEmpty())
            return null;
        return recordsList.get(recordsList.size() - 1);
    }

    public int get_MAX_RECORDS() {
        return MAX_RECORDS;
    }

}
