package com.example.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;
    private TextView profilePincode;
    private TextView profilePassword;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String phoneNumber = getIntent().getStringExtra("phone");

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);
        profilePincode = findViewById(R.id.profilePincode);
        profilePassword = findViewById(R.id.profilePassword);
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, editProfile.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, MainActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        // Initialize Firebase Auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // Reference to the current user's data in the database
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(phoneNumber);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user details from the dataSnapshot
                    String name = dataSnapshot.child("fullname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String pincode = dataSnapshot.child("pin").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    // Set the retrieved details to the TextViews
                    profileName.setText(name);
                    profileEmail.setText(email);
                    profilePhone.setText(phone);
                    profilePincode.setText(pincode);
                    profilePassword.setText(password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(profile.this, "Failed to show details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
