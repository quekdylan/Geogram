package com.dylan.geogrammad;

import com.google.android.gms.maps.model.LatLng;

public class ImageLocation {
    String ImagePath;
    LatLng Coords;
    String Caption;


    public LatLng getCoords(ImageLocation imageLocation){
        return imageLocation.Coords;
    }

    public ImageLocation(String image, LatLng coords, String caption){
        ImagePath = image;
        Coords = coords;
        Caption = caption;
    }

}
