package com.dylan.geogrammad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

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

// Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(marker.getSnippet()).child("Images");

// ImageView in your Activity
        ImageView imageView = v.findViewById(R.id.imageView);

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(v.findViewById(R.id.imageView))
                .load(storageReference)
                .into(imageView);


        TextView tvGir = (TextView) v.findViewById(R.id.tvgir);
        TextView tvDetails = (TextView) v.findViewById(R.id.tvd);
        // TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvGir.setText(marker.getSnippet());
        tvDetails.setText(marker.getTitle());
        //tvLng.setText("Longitude:"+ latLng.longitude);
        return v;
    }


}