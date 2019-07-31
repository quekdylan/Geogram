package com.dylan.geogrammad;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private String username = "";
    TextView name;

    public MapFragment() {

    }

    //sets username
    public void setUsername(String name){
        username = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        name = (TextView)view.findViewById(R.id.mapUsername);
        name.setText(username + "'s Map");

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", username);
        fragment.setArguments(bundle);

        //Initialize map
        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }

        //Add markers to map for the username passed in
        if(username != ""){
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference().child("users").child(username).child("Images");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ImageLocation imageLocation = new ImageLocation();
                        //Set attributes for imageLocation object
                        imageLocation.Caption = snapshot.child("Caption").getValue().toString();
                        imageLocation.ImagePath = snapshot.child("ImagePath").getValue().toString();
                        imageLocation.Coords = new LatLng(
                        snapshot.child("Coords").child("latitude").getValue(float.class),
                        snapshot.child("Coords").child("longitude").getValue(float.class));
                        //Add marker
                        mMap.addMarker(new MarkerOptions().position(imageLocation.Coords)
                                .title(imageLocation.Caption).snippet(imageLocation.ImagePath));
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Error", Toast.LENGTH_LONG ).show();
                }
            });
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Setting a custom info window adapter for the google map
        InfoWindowAdapter markerInfoWindowAdapter = new InfoWindowAdapter(getContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
        googleMap.setOnInfoWindowClickListener(this);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "Gir Forest Clicked!!!!", Toast.LENGTH_SHORT).show();
    }
}


/////////////////////////////////////////////////////////////////////////////////////////


