package com.dylan.geogrammad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    Button createBtn;
    TextView cancelTV;
    EditText usernameTxt;
    EditText passwordTxt;
    private ProgressBar spinner;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        cancelTV = (TextView)findViewById(R.id.cancelTV);
        createBtn = (Button)findViewById(R.id.createBtn);
        usernameTxt = (EditText)findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        spinner = (ProgressBar)findViewById(R.id.spinner2);
        spinner.setVisibility(View.GONE);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if(isValidUsername(username) && isValidPassword(password)) {
                    spinner.setVisibility(View.VISIBLE);
                    checkIfUserExists(username, password);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid Username/Password", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }
    //Check if username contains at least 6 characters
    public boolean isValidUsername (String username){
        Pattern userNamePattern;
        Matcher userNameMatcher;

        userNamePattern = Pattern.compile("^(?=.*[0-9a-zA-Z]).{6,20}$");
        userNameMatcher = userNamePattern.matcher(username);
        Log.v("tag", "Username: " + userNameMatcher.matches());
        return userNameMatcher.matches();
    }

    //Check if password contains at least 6 characters, and at least 1 number
    public boolean isValidPassword (String password) {
        Pattern passwordPattern;
        Matcher passwordMatcher;
        passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{6,20}$");
        passwordMatcher = passwordPattern.matcher(password);
        Log.v("tag", "Username: " + passwordMatcher.matches());
        return passwordMatcher.matches();
    }

    //Check if user exists in firebase
    public void checkIfUserExists (final String username, final String password) {
        final DatabaseReference myRef = database.getReference().child("users").child(username);
        final User user = new User(username, password);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(!dataSnapshot.hasChild("username")){
                    addUser(user);
               }
               else{
                   Toast.makeText(getApplicationContext(),"User already exists", Toast.LENGTH_LONG ).show();
                   spinner.setVisibility(View.GONE);
               }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }
        });
    }
    //Add user to firebase
    public void addUser(User user){
        final DatabaseReference myRef = database.getReference().child("users").child(user.username);
        myRef.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent (getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Successful. Please log in", Toast.LENGTH_SHORT ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Could not connect", Toast.LENGTH_LONG ).show();
                    }
                });
    }
}
