package com.sfaci.contacts;

import android.app.Activity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.maps.MapView;

public class MapActivity extends Activity {

    MapView mapaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapboxAccountManager.start(this, "pk.eyJ1Ijoic2ZhY2kiLCJhIjoiY2pwdTZiNDcxMDQ3NTQ4cXJicW51a2VjMSJ9.eeBDqqDQ8ELlz34avq9awg");

        setContentView(R.layout.activity_map);
        mapaView = findViewById(R.id.mapView);
        mapaView.onCreate(savedInstanceState);
    }
}
