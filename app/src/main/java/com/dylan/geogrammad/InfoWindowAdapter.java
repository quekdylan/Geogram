package com.dylan.geogrammad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public InfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.gir_forest, null);

        TextView tvGir = (TextView) v.findViewById(R.id.tvgir);
        TextView tvDetails = (TextView) v.findViewById(R.id.tvd);
        // TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvGir.setText("Gir Forest is located in the Gujarat State of India");
        tvDetails.setText("Forest is maily known for the Asiatic Lions.");
        //tvLng.setText("Longitude:"+ latLng.longitude);
        return v;
    }


}