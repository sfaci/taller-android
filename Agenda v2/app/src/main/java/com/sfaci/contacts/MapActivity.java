package com.sfaci.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Mapa para visualizar la ubicación del evento seleccionado en la lista
 * @author Santiago Faci
 * @version Taller Android Junio 2019
 */
public class MapActivity extends Activity implements OnMapReadyCallback, MapboxMap.OnMapClickListener {

    MapView mapView;
    MapboxMap mapboxMap;
    double latitude, longitude;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapboxAccountManager.start(this, "pk.eyJ1Ijoic2ZhY2kiLCJhIjoiY2pwdTZiNDcxMDQ3NTQ4cXJicW51a2VjMSJ9.eeBDqqDQ8ELlz34avq9awg");

        // Recoge los datos que le ha pasado la anterior Activity
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;
        this.mapboxMap.addMarker(new MarkerOptions()
                .setPosition(new LatLng(latitude, longitude))
                .setTitle(name));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)) // Fija la posición
                .zoom(15) // Fija el nivel de zoom
                .tilt(30) // Fija la inclinación de la cámara
                .build();
        this.mapboxMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(
                        cameraPosition), 7000);

        mapboxMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Eiffel Tower")
        );
    }
}
