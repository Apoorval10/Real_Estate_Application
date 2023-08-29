package com.example.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity {

    private EditText editName;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editPincode;
    private EditText editPassword;
    private Button saveButton;
    private ImageButton backButton;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        String phoneNumber = getIntent().getStringExtra("phone");


        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editPincode = findViewById(R.id.editPincode);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editProfile.this, profile.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Reference to the current user's data in the database
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(phoneNumber);

        // Retrieve current user's details and populate EditText fields
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

                    // Set the retrieved details to the EditText fields
                    editName.setText(name);
                    editEmail.setText(email);
                    editPhone.setText(phone);
                    editPincode.setText(pincode);
                    editPassword.setText(password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

        // Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
                Intent intent = new Intent(editProfile.this, profile.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveProfileChanges() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String pincode = editPincode.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Update the user data in the Firebase Realtime Database
        userRef.child("fullname").setValue(name);
        userRef.child("email").setValue(email);
        userRef.child("phone").setValue(phone);
        userRef.child("pin").setValue(pincode);
        userRef.child("password").setValue(password);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
