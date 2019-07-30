package com.dylan.geogrammad;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FriendsFragment extends Fragment {

    private RecyclerView myFriendsList;

    private DatabaseReference FriendsReference;
    private FirebaseAuth mAuth;

    private View myMainView;

    public FriendsFragment(){
        //required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myMainView = inflater.inflate(R.layout.fragment_friends,container,false);

        myFriendsList = (RecyclerView) myMainView.findViewById(R.id.friends_list);

        FriendsReference = FirebaseDatabase.getInstance().getReference().child("users");

        myFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));


        //inflate layout for this fragment
        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FriendsReference, User.class)
                .build();

        FirebaseRecyclerAdapter<User, FriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, FriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull User model)
                    {
                        holder.userName.setText(model.getUsername());



                    }

                    @NonNull
                    @Override
                    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row,viewGroup,false);
                        FriendsViewHolder viewHolder = new FriendsViewHolder(view);


                        //on CLICK starts here. itemView links to "R.id.teamfriend" in row.xml
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                MapFragment map = new MapFragment();
                                map.setUsername("thanks");
                                loadFragment(map);
                            }
                        });
                        return viewHolder;
                    }
                };

        myFriendsList.setAdapter(adapter);

        adapter.startListening();


    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName;


        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.teamfriend);

        }
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //mappContainer is the ID in fragment_map.xml
        transaction.replace(R.id.mappContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
