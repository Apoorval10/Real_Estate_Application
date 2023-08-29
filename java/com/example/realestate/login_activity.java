package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_activity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://realestate-c185f-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final EditText phone = findViewById(R.id.login_phone);
        final EditText password = findViewById(R.id.login_password);
        final Button loginBtn = findViewById(R.id.login_button);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phonetxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                final String path = "users";

                if (phonetxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(login_activity.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child(path).child(phonetxt).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                if(task.getResult().getValue() == null){
                                    Toast.makeText(login_activity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }else{

                                    final String fbvalue = task.getResult().getValue().toString();
                                    String[] separated = fbvalue.split(",");
                                    String separated1 = separated[0];
                                    String[] password = separated1.split("password=");
                                    String separatedpassword = password[1];
                                    if (separatedpassword.equals(passwordTxt) ){
                                       Intent intent =new Intent(login_activity.this, MainActivity.class);
                                        intent.putExtra("phone", phonetxt);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(login_activity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
//
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // open register activity
                startActivity(new Intent(login_activity.this, sign_up_activity.class));
            }
        });

    }
}