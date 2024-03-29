package com.dylan.geogrammad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

//https://camerakit.io/docs

public class CameraFragment extends Fragment {

    CameraKitView cameraKitView;
    ImageButton shutter;
    ImageButton cameraToggle;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        cameraKitView =  view.findViewById(R.id.camera);
        shutter = view.findViewById(R.id.shutterBtn);
        shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // capture image and pass it to photo preview fragment
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @Override
                    public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                        File savedPhoto = new File(getContext().getFilesDir(), "uploadPhoto.jpg");
                        try {
                            Fragment photoPreview = new PhotoPreviewFragment();
                            Bundle args = new Bundle();
                            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                            outputStream.write(capturedImage);
                            outputStream.close();
                            args.putString("Image", savedPhoto.getAbsolutePath());
                            photoPreview.setArguments(args);
                            getFragmentManager().beginTransaction().replace(R.id.container, photoPreview).addToBackStack(null).commit();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        // toggle camera facing
        cameraToggle = view.findViewById(R.id.cameraToggle);
        cameraToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraKitView.toggleFacing();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
