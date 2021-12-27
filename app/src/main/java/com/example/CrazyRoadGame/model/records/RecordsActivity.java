package com.example.CrazyRoadGame.model.records;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.CrazyRoadGame.MyDB;
import com.example.CrazyRoadGame.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.textfield.TextInputLayout;

public class RecordsActivity extends AppCompatActivity {

    private TextInputLayout record_EDT_inputName;
    private Button record_BTN_enter;

    private Bundle bundle;

    private ListFragment RF;
    private MapFragment MF;

    private CallBack_MapFrag callBack_MF;
    private CallBack_ListFrag callBack_LF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        bundle = getIntent().getBundleExtra("toRecord");
        findViews();
        initViews();
    }

    private void findViews() {
        RF = new ListFragment();
        RF.setActivity(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.record_FRG_recordFragment, RF)
                .commit();

        record_EDT_inputName = findViewById(R.id.record_EDT_inputName);
        record_BTN_enter = findViewById(R.id.record_BTN_enter);
    }

    private void findMapView() {
        MF = new MapFragment();
        MF.setActivity(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.record_FRG_mapFragment, MF)
                .commit();
    }

    private void initViews() {
        if (bundle.getBoolean("isEnterRT", false)) {
            record_EDT_inputName.setVisibility(View.VISIBLE);
            record_BTN_enter.setVisibility(View.VISIBLE);

            record_BTN_enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (record_EDT_inputName.getEditText().getText().toString().length() < 3)
                        record_EDT_inputName.setError("min 3 characters");
                    else {
                        callBack_LF.addNewRecord(getRecord());
                        record_BTN_enter.setVisibility(View.GONE);
                        record_EDT_inputName.setVisibility(View.GONE);
                        findMapView();
                    }
                }
            });
        } else {
            findMapView();
            record_EDT_inputName.setVisibility(View.GONE);
            record_BTN_enter.setVisibility(View.GONE);
        }
    }

    public void setCallBack_LF(CallBack_ListFrag callBack_LF) {
        this.callBack_LF = callBack_LF;
    }

    public void setCallBack_MF(CallBack_MapFrag callBack_MF) {
        this.callBack_MF = callBack_MF;
    }

    private Record getRecord() {
        Record newRecord = new Record()
                .setTime()
                .setPlayerName(record_EDT_inputName.getEditText().getText().toString())
                .setScore(bundle.getInt("score"))
                .setLat(bundle.getDouble("lat",0))
                .setLon(bundle.getDouble("lon",0));
        return newRecord;
    }

    public void onMapButtonClicked(int position) {
        double lat = MyDB.getInstance().getAllRecords().get(position).getLat();
        double lon = MyDB.getInstance().getAllRecords().get(position).getLon();
        callBack_MF.changePosition(lat, lon);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}



