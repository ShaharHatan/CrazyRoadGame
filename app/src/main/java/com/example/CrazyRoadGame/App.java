package com.example.CrazyRoadGame;

import android.app.Application;

import com.example.CrazyRoadGame.model.records.Record;
import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //c'tor MSP
        MSP.init(this);

        //get the records from MSP as json file
        String json = MSP.getInstance().getStringSP("my_db", "");

        //make the records json file into MyDB object
        MyDB newDb = new Gson().fromJson(json, MyDB.class);
        if (newDb == null)
            MyDB.init();

            //c'tor MyDB and set all the records from MSP
       else
            MyDB.init().setRecordList(newDb.getAllRecords());


    }
}
