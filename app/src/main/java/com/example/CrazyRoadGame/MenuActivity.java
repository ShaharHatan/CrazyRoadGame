package com.example.CrazyRoadGame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.CrazyRoadGame.model.records.RecordsActivity;
import com.example.CrazyRoadGame.model.game.GameActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MenuActivity extends AppCompatActivity {

    private TextView menu_TXT_mainTitle;
    private TextView menu_TXT_secondTitle;
    private ImageView menu_IMG_logo;
    private Button menu_BTN_startGameButton;
    private SwitchMaterial menu_SWC_sensorMode;
    private Button menu_BTN_recordsTableButton;
    private RadioButton menu_RDB_radioEasy;
    private RadioButton menu_RDB_radioHard;


    private double lat = 0;
    private double lon = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();
        getLocationPermission();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationPermission();
    }

    private void findViews() {
        menu_TXT_mainTitle = findViewById(R.id.menu_TXT_mainTitle);
        menu_TXT_secondTitle = findViewById(R.id.menu_TXT_secondTitle);
        menu_IMG_logo = findViewById(R.id.menu_IMG_logo);
        menu_BTN_startGameButton = findViewById(R.id.menu_BTN_startGameButton);
        menu_SWC_sensorMode = findViewById(R.id.menu_SWC_sensorMode);
        menu_BTN_recordsTableButton = findViewById(R.id.menu_BTN_recordsTableButton);
        menu_RDB_radioEasy = findViewById(R.id.menu_RDB_radioEasy);
        menu_RDB_radioHard = findViewById(R.id.menu_RDB_radioHard);
    }

    private void initViews() {
        Glide
                .with(this)
                .load(R.drawable.ic_launcher_foreground)
                .into(menu_IMG_logo);

        menu_BTN_startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        menu_BTN_recordsTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecordsTable();
            }
        });

        menu_RDB_radioEasy.setChecked(true);
    }

    private void startGame() {
        //the first run never find the location
        //for that reason we request permission again if location 0:0
       if(lat==0&&lon==0)
            getLocationPermission();
        Intent intentGame = new Intent(this, GameActivity.class);
        Bundle bundle = new Bundle();
        if (menu_RDB_radioEasy.isChecked())
            bundle.putInt("difficulty", GameActivity.ROCK_DELAY_EASY);
        else
            bundle.putInt("difficulty", GameActivity.ROCK_DELAY_HARD);
        bundle.putBoolean("isAccSensor", menu_SWC_sensorMode.isChecked());
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        intentGame.putExtra("location", bundle);
        locationManager.removeUpdates(locationListener);
        this.startActivity(intentGame);
    }

    private void showRecordsTable() {
        Intent intentRecords = new Intent(this, RecordsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEnterRT", false);
        intentRecords.putExtra("toRecord", bundle);
        this.startActivity(intentRecords);
    }


    private void getLocationPermission() {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
            }
        }
}
