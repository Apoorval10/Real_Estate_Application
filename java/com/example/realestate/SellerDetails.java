package com.example.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SellerDetails extends AppCompatActivity {

    TextView sellername,sellerPhone;
    Button callBtn,whatsappBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);


        sellername = findViewById(R.id.sellerName);
        sellerPhone = findViewById(R.id.sellerPhoneNumber);
        callBtn = findViewById(R.id.call);
        whatsappBtn = findViewById(R.id.Whatsapp);
        ImageButton backButton = findViewById(R.id.backButton);
        String headline=getIntent().getStringExtra("singleHeadline");
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
                Intent intent = new Intent(SellerDetails.this, SinglePropertyActivity2.class);
                intent.putExtra("singleHeadline", headline);
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
        sellername.setText(getIntent().getStringExtra("singleSeller"));
        sellerPhone.setText(getIntent().getStringExtra("singleSellerPhone"));
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = sellerPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = sellerPhone.getText().toString();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + phoneNumber));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SellerDetails.this, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}