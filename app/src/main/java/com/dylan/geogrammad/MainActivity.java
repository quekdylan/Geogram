package com.dylan.geogrammad;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request location permission
        ActivityCompat.requestPermissions(this ,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Bottom nav bar
        BottomNavigationView navbar = findViewById(R.id.bottomNavBar);
        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.friendsNav:
                        loadFragment(new FriendsFragment());
                        return true;
                    case R.id.cameraNav:
                        loadFragment(new CameraFragment());
                        return true;
                    case R.id.accountNav:
                        loadFragment(new AccountFragment());
                        return true;
                }
                return false;
            }
        });

        loadFragment(new FriendsFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
