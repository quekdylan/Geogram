package com.dylan.geogrammad;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dylan.geogrammad.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Button btn;
    private Marker marker;
    private List<ImageLocation> images = new LinkedList<>();

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        btn = view.findViewById(R.id.closeAllBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //Retrieve ImageLocations from firebase and store into images list
    public void loadMap (final String username){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("users").child(username).child("Images");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageLocation imageLocation = new ImageLocation();
                    imageLocation.Caption = snapshot.child("Caption").getValue().toString();
                    imageLocation.ImagePath = snapshot.child("ImagePath").getValue().toString();
                    imageLocation.Coords = new LatLng(
                            snapshot.child("Coords").child("latitude").getValue(float.class),
                            snapshot.child("Coords").child("longitude").getValue(float.class));
                    images.add(imageLocation);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add marker for each Imagelocation in images list
        for (ImageLocation image : images){
            mMap.addMarker(new MarkerOptions().position(image.Coords).title(image.Caption));
            Log.e("Image", image.Caption);
            Log.e("Image", image.Coords.toString());
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Gir Forest Clicked!!!!", Toast.LENGTH_SHORT).show();
    }
}


/////////////////////////////////////////////////////////////////////////////////////////


