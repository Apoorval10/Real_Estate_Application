package com.example.realestate;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class singleProperty extends AppCompatActivity {

    TextView singleHeadline, singlePrice, singleSurface, singleDiscription, singleRooms, singleLocation, singlePof, singlePin;
    ImageView singleImage;
    Button sellerDetails, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_property);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("properties");

        singleHeadline = findViewById(R.id.singleHeadline);
        singlePrice = findViewById(R.id.singlePrice);
        singleSurface = findViewById(R.id.singleSurface);
        singleDiscription = findViewById(R.id.singleDiscription);
        singleRooms = findViewById(R.id.singleRooms);
        singleLocation = findViewById(R.id.singleLocation);
        singlePin = findViewById(R.id.singlePin);
        singlePof = findViewById(R.id.singlePof);
        singleImage = findViewById(R.id.singleImage);
        sellerDetails = findViewById(R.id.Submit);
        deleteButton = findViewById(R.id.delete);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(singleProperty.this, myProperties.class);
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                startActivity(intent);
                finish();
            }
        });

        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.img3)
                .into(singleImage);

        String headline = getIntent().getStringExtra("singleHeadline");
        String id = getIntent().getStringExtra("singleid");
        String price = getIntent().getStringExtra("singlePrice");
        String surface = getIntent().getStringExtra("singleSurface");
        String discription = getIntent().getStringExtra("singleDiscription");
        String rooms = getIntent().getStringExtra("singleRooms");
        String location = getIntent().getStringExtra("singleLocation");
        String pin = getIntent().getStringExtra("singlePin");
        String pof = getIntent().getStringExtra("singlePof");
        singleHeadline.setText(headline);
        singlePrice.setText(price);
        singleSurface.setText(surface);
        singleDiscription.setText(discription);
        singleRooms.setText(rooms);
        singleLocation.setText(location);
        singlePin.setText(pin);
        singlePof.setText(pof);
        String singleSeller = getIntent().getStringExtra("singleSeller");
        String singleSellerPhone = getIntent().getStringExtra("singleSellerPhone");

        sellerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(singleProperty.this, editProperty.class);
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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference propertyRef = databaseReference.child("users").child(getIntent().getStringExtra("phone")).child("properties").child(id);
                propertyRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(singleProperty.this, "Property deleted successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(singleProperty.this, myProperties.class);
                                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(singleProperty.this, "Failed to delete property: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}






