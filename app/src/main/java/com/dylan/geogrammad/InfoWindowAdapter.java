package com.dylan.geogrammad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.firebase.ui.storage.images.FirebaseImageLoader;
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

        //get username from image path
        String imagePath = marker.getSnippet();
        String username = imagePath.substring(0, imagePath.indexOf("/"));

        // Reference to an image file in Cloud Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference pathReference = storageRef.child(imagePath);


        ImageView image = (ImageView)v.findViewById(R.id.imageView1);

        // Load the image using Glide
        Glide.with(v.getContext())
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(image);

        TextView tvGir = (TextView) v.findViewById(R.id.tvgir);
        TextView tvDetails = (TextView) v.findViewById(R.id.tvd);
        // TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
        tvGir.setText(username);
        tvDetails.setText(marker.getTitle());
        //tvLng.setText("Longitude:"+ latLng.longitude);
        return v;
    }

}