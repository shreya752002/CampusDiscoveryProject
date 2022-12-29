package com.campusdiscovery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.campusdiscovery.app.databinding.ActivityEventMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EventMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityEventMapBinding binding;
    private ArrayList<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (MainActivity.check) {
            events = MainActivity.filteredEvents;
        } else {
            events = MainActivity.eventsList;
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (events.size() == 0) {
            LatLng gtech = new LatLng(33.7722, -84.3902);
            mMap.addMarker(new MarkerOptions().position(gtech).title("Georgia Tech"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gtech, 14.0f));
        } else {
            for (int i = 0; i < events.size(); i++) {
                System.out.println(events.get(i).eventName);
                mMap.addMarker(new MarkerOptions().position(events.get(i).location.coordinates).
                        title(events.get(i).eventName));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(events.get(i).location.coordinates,
                        14.0f));
            }
        }
        //new code
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                return openOnMarkerClick(marker);
            }
        });
        //mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        //float zoom = 16.0f;
        mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(20.0f);
    }

    //new code
    public boolean openOnMarkerClick(Marker marker) {
        String name = marker.getTitle();
        EventScreen.selectedEvent = MainActivity.events.get(name);
        Intent intent = new Intent(this, ExpandedEvent.class);
        startActivity(intent);
        return false;
    }
}