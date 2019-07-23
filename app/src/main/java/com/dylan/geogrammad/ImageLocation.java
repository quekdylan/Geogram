package com.dylan.geogrammad;

import com.google.android.gms.maps.model.LatLng;

public class ImageLocation {
    public String ImagePath;
    public LatLng Coords;

    public ImageLocation(String image, LatLng coords){
        ImagePath = image;
        Coords = coords;
    }

}