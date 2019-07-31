package com.dylan.geogrammad;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import im.delight.android.location.SimpleLocation;

public class PhotoPreviewFragment extends Fragment {

    ImageView photoPreview;
    ImageButton closeBtn;
    ImageButton checkBtn;
    EditText captionTxt;
    private SimpleLocation location;
    private Session session;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_preview, container, false);

        closeBtn = (ImageButton)view.findViewById(R.id.closeBtn);
        checkBtn = (ImageButton)view.findViewById(R.id.checkBtn);
        photoPreview = (ImageView)view.findViewById(R.id.imageView2);
        captionTxt = (EditText) view.findViewById(R.id.captionTxt);

        location = new SimpleLocation(getContext());
        // if GPS is not enabled
        if (!location.hasLocationEnabled()) {
            Toast.makeText(getContext(),"Please enable GPS", Toast.LENGTH_LONG ).show();
            SimpleLocation.openSettings(getContext());
        }
        location.beginUpdates();

        //retrieve photo to display
        final String imgPath = getArguments().getString("Image");
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
        photoPreview.setImageBitmap(bitmap);

        // cancel
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CameraFragment());
            }
        });

        // confirm
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get location and save ImageLocation object
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                location.endUpdates();

                String caption = captionTxt.getText().toString();

                LatLng latLng = new LatLng(latitude, longitude);
                ImageLocation imageLocation = new ImageLocation(imgPath, latLng, caption);
                addImage(imageLocation);
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addImage (ImageLocation imageLocation) {

        //Upload image to firebase storage
        session = new Session(getContext());
        Uri file = Uri.fromFile(new File(imageLocation.ImagePath));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //generate unique id for image
        String uuid = UUID.randomUUID().toString();
        StorageReference storageRef = storage.getReference().child(session.getUsername()).child(uuid);
        UploadTask uploadTask = storageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(),"Error when uploading image", Toast.LENGTH_LONG ).show();
                loadFragment(new CameraFragment());
            }
        });

        //Upload record to firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(session.getUsername()).child("Images");
        // Update image path to firebase image path
        imageLocation.ImagePath = session.getUsername() + "/" + uuid;
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put(uuid, imageLocation);
        myRef.updateChildren(hopperUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getContext(),"Error when uploading image", Toast.LENGTH_LONG ).show();
                    loadFragment(new CameraFragment());
                } else {
                    Toast.makeText(getContext(),"Image uploaded!", Toast.LENGTH_SHORT ).show();
                    loadFragment(new AccountFragment());
                }
            }
        });
    }

}





