package com.example.CrazyRoadGame.model.records;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public interface CallBack_MapFrag extends OnMapReadyCallback {
    @Override
    void onMapReady(@NonNull GoogleMap googleMap);
    void changePosition(double lat, double lon);
}
