package com.dylan.geogrammad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView newUserTxtView;
    Button loginBtn;
    EditText usernameTxt;
    EditText passwordTxt;
    private ProgressBar spinner;
    private Session session;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newUserTxtView = (TextView)findViewById(R.id.newUserTxtView);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        usernameTxt = (EditText)findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        spinner = (ProgressBar)findViewById(R.id.spinner);
        session = new Session(getApplicationContext());
        spinner.setVisibility(View.GONE);

        // Auto login if user has logged in before
        if(session.checkLogin()) {
            Intent intent = new Intent (getApplicationContext(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Welcome back, "+session.getUsername(), Toast.LENGTH_LONG ).show();
        }

        // Redirect to isValidLogin method if inputs are not empty
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                String username = usernameTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                if(!username.equals("") && !password.equals("")){
                    isValidLogin(username, password);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please enter username and password!", Toast.LENGTH_LONG ).show();
                    spinner.setVisibility(View.GONE);
                }
            }
        });

        newUserTxtView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent (getApplicationContext(),CreateAccountActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }

    //Checks if user credentials are valid
    public void isValidLogin (final String username, final String password){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users");
        Query userQuery = myRef.orderByKey().equalTo(username);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    User user = singleSnapshot.getValue(User.class);
                    //Valid
                    if (user.username.equals(username) && user.password.equals(password)) {
                        session.logout();
                        session.setUser(username,password);
                        Intent intent = new Intent (getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Welcome back, "+username, Toast.LENGTH_LONG ).show();
                    }
                    //Invalid
                    else{
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Invalid Credentials", Toast.LENGTH_LONG ).show();
                    }
                }
            }
            //Database error
            @Override
            public void onCancelled(DatabaseError databaseError) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Could not connect", Toast.LENGTH_LONG ).show();
            }
        });
    }
}
