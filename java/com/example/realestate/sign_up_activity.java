package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class sign_up_activity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://realestate-c185f-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final EditText fullname = findViewById(R.id.signup_name);
        final EditText email = findViewById(R.id.signup_email);
        final EditText phone = findViewById(R.id.signup_phone);
        final EditText pin = findViewById(R.id.signup_pin);
        final EditText password = findViewById(R.id.signup_password);
        final Button registerBtn = findViewById(R.id.signup_button);
        final TextView loginNowBtn = findViewById(R.id.loginNow);
        final EditText confirmPasswordEditText = findViewById(R.id.signup_conpassword);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input
                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String pinTxt = pin.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPassword = confirmPasswordEditText.getText().toString();

                // Validate input fields
                if (fullnameTxt.isEmpty() || emailTxt.isEmpty() || pinTxt.isEmpty() || confirmPassword.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(sign_up_activity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPhone(phoneTxt)) {
                    Toast.makeText(sign_up_activity.this, "Enter a valid phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isValidPin(pinTxt)) {
                    Toast.makeText(sign_up_activity.this, "Enter a valid PinCode", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isValidEmail(emailTxt)) {
                    Toast.makeText(sign_up_activity.this, "Enter a valid email ID", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isValidPassword(passwordTxt)) {
                    Toast.makeText(sign_up_activity.this, "Password must contain at least 1 number, 1 special character, and minimum of 6 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!passwordTxt.equals(confirmPassword)) {
                    Toast.makeText(sign_up_activity.this, "Both passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the phone number already exists in the database
                DatabaseReference userRef = databaseReference.child("users").child(phoneTxt);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Phone number already exists
                            Toast.makeText(sign_up_activity.this, "Phone number already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // Phone number doesn't exist, proceed with user registration

                            // Create user in Firebase Authentication
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt, passwordTxt)
                                    .addOnCompleteListener(sign_up_activity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // User registration successful
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                // Store user details in the Realtime Database
                                                DatabaseReference userRef = databaseReference.child("users").child(phoneTxt);
                                                userRef.child("fullname").setValue(fullnameTxt);
                                                userRef.child("email").setValue(emailTxt);
                                                userRef.child("pin").setValue(pinTxt);
                                                userRef.child("phone").setValue(phoneTxt);
                                                userRef.child("password").setValue(passwordTxt);

                                                Toast.makeText(sign_up_activity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(sign_up_activity.this, login_activity.class);
                                                intent.putExtra("name", fullnameTxt);
                                                intent.putExtra("email", emailTxt);
                                                intent.putExtra("password", passwordTxt);
                                                intent.putExtra("phone", phoneTxt);
                                                intent.putExtra("pincode", pinTxt);
                                                intent.putExtra("user", user);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // User registration failed
                                                Toast.makeText(sign_up_activity.this, "User registration failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error occurred while accessing the database
                        Toast.makeText(sign_up_activity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_up_activity.this, login_activity.class));
                finish();
            }
        });

    }

    Pattern number = Pattern.compile("^.*[0-9].*$");
    Pattern specialCharacter = Pattern.compile("^.*[^a-zA-Z0-9].*$");
    Pattern mobile = Pattern.compile("^[6-9]\\d{9}$");

    Pattern emailid = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private Boolean isValidPassword(String password) {
        if (password.length() < 6) {
            return false;
        }

        if (!number.matcher(password).matches()) {
            return false;
        }
        if (!specialCharacter.matcher(password).matches()) {
            return false;
        }
        return true;
    }

    private Boolean isValidPhone(String Phone) {
        if (Phone.length() != 10) {
            return false;
        }

        if (!mobile.matcher(Phone).matches()) {
            return false;
        }
        return true;
    }
    private Boolean isValidPin(String Pin) {
        if (Pin.length() != 6) {
            return false;
        }
        return true;
    }
    private Boolean isValidEmail(String email) {

        if (!emailid.matcher(email).matches()) {
            return false;
        }
        return true;
    }
}