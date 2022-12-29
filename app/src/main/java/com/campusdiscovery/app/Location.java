package com.campusdiscovery.app;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    String location;
    LatLng coordinates;

    public Location(String location, LatLng coordinates) {
        this.location = location;
        this.coordinates = coordinates;
    }
}
