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

public class editProperty extends AppCompatActivity {

    private EditText editheadline,editSname,editSnumber;
    private EditText editprice,edittype,editdesc,editrooms,editlocation,editpin,editpof;

    private Button saveButton;
    private ImageButton backButton;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);
        String phoneNumber = getIntent().getStringExtra("phone");


        editheadline = findViewById(R.id.editheadline);
        editprice = findViewById(R.id.editPrice);
        edittype = findViewById(R.id.editType);
        editdesc = findViewById(R.id.editDescription);
        editrooms = findViewById(R.id.editBedrooms);
        editlocation = findViewById(R.id.editLocation);
        editpin = findViewById(R.id.editPin);
        editpof = findViewById(R.id.editPof);
        editSname = findViewById(R.id.editSname);
        editSnumber = findViewById(R.id.editSnumber);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);
        String headline=getIntent().getStringExtra("singleHeadline");
        String id=getIntent().getStringExtra("singleid");
        String price=getIntent().getStringExtra("singlePrice");
        String surface=getIntent().getStringExtra("singleSurface");
        String discription=getIntent().getStringExtra("singleDiscription");
        String rooms=getIntent().getStringExtra("singleRooms");
        String location=getIntent().getStringExtra("singleLocation");
        String pin=getIntent().getStringExtra("singlePin");
        String pof=getIntent().getStringExtra("singlePof");
        String singleSeller=getIntent().getStringExtra("singleSeller");
        String singleSellerPhone= getIntent().getStringExtra("singleSellerPhone");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editProperty.this, myProperties.class);
                intent.putExtra("singleHeadline", headline);
                intent.putExtra("singleid", id);
                intent.putExtra("singlePrice", price);
                intent.putExtra("singleSurface", surface);
                intent.putExtra("singleDiscription", discription);
                intent.putExtra("singleRooms", rooms);
                intent.putExtra("singleLocation", location);
                intent.putExtra("singlePin", pin);
                intent.putExtra("singlePof", pof);
                intent.putExtra("singleSeller", singleSeller);
                intent.putExtra("singleSellerPhone", singleSellerPhone);
                intent.putExtra("singleImage", getIntent().getStringExtra("singleImage"));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                startActivity(intent);
                finish();
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("properties").child("users").child(phoneNumber).child("properties").child(id);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve user details from the dataSnapshot
                    String headline = dataSnapshot.child("headline").getValue(String.class);
                    String price = dataSnapshot.child("price").getValue(String.class);
                    String type = dataSnapshot.child("surface").getValue(String.class);
                    String desc = dataSnapshot.child("discription").getValue(String.class);
                    String rooms = dataSnapshot.child("rooms").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String pin = dataSnapshot.child("pin").getValue(String.class);
                    String pof = dataSnapshot.child("pof").getValue(String.class);
                    String sname = dataSnapshot.child("seller").getValue(String.class);
                    String number = dataSnapshot.child("sellerPhone").getValue(String.class);

                    // Set the retrieved details to the EditText fields
                    editheadline.setText(headline);
                    editprice.setText(price);
                    edittype.setText(type);
                    editdesc.setText(desc);
                    editrooms.setText(rooms);
                    editlocation.setText(location);
                    editpin.setText(pin);
                    editpof.setText(pof);
                    editSname.setText(sname);
                    editSnumber.setText(number);
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
                Intent intent = new Intent(editProperty.this, myProperties.class);
                intent.putExtra("singleHeadline", headline);
                intent.putExtra("singleid", id);
                intent.putExtra("singlePrice", price);
                intent.putExtra("singleSurface", surface);
                intent.putExtra("singleDiscription", discription);
                intent.putExtra("singleRooms", rooms);
                intent.putExtra("singleLocation", location);
                intent.putExtra("singlePin", pin);
                intent.putExtra("singlePof", pof);
                intent.putExtra("singleSeller", singleSeller);
                intent.putExtra("singleSellerPhone", singleSellerPhone);
                intent.putExtra("singleImage", getIntent().getStringExtra("singleImage"));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveProfileChanges() {
        String headline = editheadline.getText().toString().trim();
        String price = editprice.getText().toString().trim();
        String type = edittype.getText().toString().trim();
        String desc = editdesc.getText().toString().trim();
        String rooms = editrooms.getText().toString().trim();
        String location = editlocation.getText().toString().trim();
        String pin = editpin.getText().toString().trim();
        String pof = editpof.getText().toString().trim();
        String sname = editSname.getText().toString().trim();
        String snumber = editSnumber.getText().toString().trim();

        // Update the user data in the Firebase Realtime Database
        userRef.child("headline").setValue(headline);
        userRef.child("price").setValue(price);
        userRef.child("surface").setValue(type);
        userRef.child("discription").setValue(desc);
        userRef.child("rooms").setValue(rooms);
        userRef.child("location").setValue(location);
        userRef.child("pinCode").setValue(pin);
        userRef.child("pof").setValue(pof);
        userRef.child("seller").setValue(sname);
        userRef.child("sellerPhone").setValue(snumber);

        Toast.makeText(this, "Property updated successfully", Toast.LENGTH_SHORT).show();
    }
}
