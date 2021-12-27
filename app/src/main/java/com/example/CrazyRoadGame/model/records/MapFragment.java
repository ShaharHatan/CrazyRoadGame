package com.example.CrazyRoadGame.model.records;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.CrazyRoadGame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements CallBack_MapFrag{
    private SupportMapFragment SMF;
    private RecordsActivity activity_RA;
    private GoogleMap googleMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        activity_RA.setCallBack_MF(this);

        findMapView();
        initMapView();

        return view;
    }

    public MapFragment() {
    }

    private void findMapView() {
        //initialize map fragment
        //use SupportMapFragment for using in fragment instead of activity
        SMF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragMap_FRG);
    }

    private void initMapView() {
        //Sets a callback object which will be triggered when the...
        //....GoogleMap instance is ready to be used (trigger when onMapReady())
        SMF.getMapAsync(this);
    }

    public void setActivity(RecordsActivity recordsActivity) {
        this.activity_RA = recordsActivity;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
    }

    @Override
    public void changePosition(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);
        //initialize the marker option
        MarkerOptions markerOptions = new MarkerOptions();
        //set position of the marker
        markerOptions.position(latLng);
        //set title of the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        //clear old markers
        googleMap.clear();
        //animating to zoom the marker
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 10));
        //add marker on map
        googleMap.addMarker(markerOptions);
    }


}


