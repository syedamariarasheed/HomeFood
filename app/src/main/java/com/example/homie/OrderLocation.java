package com.example.homie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderLocation extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    MapView mapView;
    String latLngget;
    String[] latLng;
    String latitude1,longitude1;
    double latitude,longitude;
    LatLng mylocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlocation);
        Intent intent=getIntent();
        latLngget =intent.getStringExtra("langlat");
        latLng = latLngget.split(",");
        longitude1=latLng[0].replaceAll("[^\\d.]", "");
        latitude1=latLng[1].replaceAll("[^\\d.]", "");
        latitude = Double.parseDouble(longitude1);
        longitude = Double.parseDouble(latitude1);
        mylocation = new LatLng(latitude, longitude);
        mapView=(MapView)findViewById(R.id.map);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mgoogleMap=googleMap;
        googleMap.addMarker(new MarkerOptions().position(mylocation).title("Here's Your friend"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,15));
    }
}
