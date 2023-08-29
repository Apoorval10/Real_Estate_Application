package com.example.realestate;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadProductActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;

    private EditText headlineEditText;
    private EditText priceEditText;
    private EditText surfaceEditText;
    private EditText roomsEditText;
    private EditText locationEditText;
    private EditText pofEditText, pinEditText, sellerPhoneEditText;
    private EditText sellerEditText, discriptionEditText;
    private Button submitButton;
    private ImageView propertyImageView;

    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        String phoneNumber = getIntent().getStringExtra("phone");

        headlineEditText = findViewById(R.id.headline);
        priceEditText = findViewById(R.id.price);
        surfaceEditText = findViewById(R.id.surface);
        discriptionEditText = findViewById(R.id.discription);
        roomsEditText = findViewById(R.id.rooms);
        locationEditText = findViewById(R.id.location);
        pinEditText = findViewById(R.id.pin);
        pofEditText = findViewById(R.id.pof);
        sellerEditText = findViewById(R.id.seller);
        sellerPhoneEditText = findViewById(R.id.sellerPhone);
        submitButton = findViewById(R.id.Submit);
        propertyImageView = findViewById(R.id.uploadbtn);
        ImageButton backButton = findViewById(R.id.backButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("properties");
        storageReference = FirebaseStorage.getInstance().getReference("property_images");

        propertyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImagePicker();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadProductActivity.this, MainActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProperty();
            }
        });
    }

    private void launchImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            propertyImageView.setImageURI(imageUri);
        }
    }

    private void uploadProperty() {
        // Get the values from the input fields
        String headline = headlineEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String surface = surfaceEditText.getText().toString().trim();
        String description = discriptionEditText.getText().toString().trim();
        String rooms = roomsEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String pinCode = pinEditText.getText().toString().trim();
        String pointOfInterest = pofEditText.getText().toString().trim();
        String sellerName = sellerEditText.getText().toString().trim();
        String sellerPhone = sellerPhoneEditText.getText().toString().trim();

        if (headline.isEmpty() || price.isEmpty() || surface.isEmpty() || description.isEmpty() || rooms.isEmpty()
                || location.isEmpty() || pinCode.isEmpty() || pointOfInterest.isEmpty() || sellerName.isEmpty() || sellerPhone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isNumeric(price)) {
            Toast.makeText(this, "Price should be a numeric value", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isIndianPhoneNumber(sellerPhone)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageName = System.currentTimeMillis() + ".jpg";
        StorageReference imageReference = storageReference.child(imageName);
        UploadTask uploadTask = imageReference.putFile(imageUri);

        progressDialog = ProgressDialog.show(UploadProductActivity.this, "Uploading", "Please wait...", true);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                String phoneNumber = getIntent().getStringExtra("phone");

                                DatabaseReference userPropertyRef = databaseReference.child("users").child(phoneNumber).child("properties");
                                String propertyKey = userPropertyRef.push().getKey();

                                ProjectModel property = new ProjectModel(
                                        propertyKey,
                                        headline,
                                        price,
                                        surface,
                                        description,
                                        rooms,
                                        location,
                                        pinCode,
                                        pointOfInterest,
                                        sellerName,
                                        sellerPhone,
                                        imageUrl
                                );

                                userPropertyRef.child(propertyKey).setValue(property)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UploadProductActivity.this, "Property uploaded successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UploadProductActivity.this, MainActivity.class);
                                                intent.putExtra("phone", phoneNumber);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UploadProductActivity.this, "Failed to upload property: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadProductActivity.this, "Failed To validate the phone number ", Toast.LENGTH_SHORT).show();

                    }


                });
    }
    private boolean isIndianPhoneNumber(String phoneNumber) {
        // Remove any spaces or special characters from the phone number
        phoneNumber = phoneNumber.replaceAll("\\s+", "");

        // Check if the phone number is exactly 10 digits and starts with a valid Indian mobile number prefix
        return phoneNumber.matches("^[6-9]\\d{9}$");
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // Matches positive or negative integers or decimals
    }

}
