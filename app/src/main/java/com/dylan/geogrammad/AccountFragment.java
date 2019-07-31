package com.dylan.geogrammad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AccountFragment extends Fragment {

    Button logoutBtn;
    private Session session;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        session = new Session(getContext());
        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                Intent intent = new Intent (getContext(),LoginActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(),"Logout successful", Toast.LENGTH_LONG ).show();
            }
        });

        //Loads map fragment for user
        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        MapFragment map = new MapFragment();
        map.setUsername(session.getUsername());
        childFragTrans.add(R.id.mapContainer, map);
        childFragTrans.addToBackStack(null);
        childFragTrans.commit();
        return view;
    }
}
