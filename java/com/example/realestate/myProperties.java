package com.example.realestate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myProperties extends AppCompatActivity {

    private SearchView searchView;

    private RecyclerView recyclerView;
    private ArrayList<ProjectModel> propertyList;

    private propertyAdapter propertyAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);

        Toolbar toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        String phoneNumber = getIntent().getStringExtra("phone");
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myProperties.this, MainActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        propertyList = new ArrayList<>();
        String phone = getIntent().getStringExtra("phone");
        propertyAdapter = new propertyAdapter(myProperties.this, propertyList,phone);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(propertyAdapter);

        DatabaseReference propertiesRef = FirebaseDatabase.getInstance().getReference("properties").child("users");

        propertiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyList.clear();

                String phoneNumber = getIntent().getStringExtra("phone");

                DataSnapshot userSnapshot = dataSnapshot.child(phoneNumber).child("properties");

                for (DataSnapshot propertySnapshot : userSnapshot.getChildren()) {
                    ProjectModel property = propertySnapshot.getValue(ProjectModel.class);
                    propertyList.add(property);
                }

                propertyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(recyclerView, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void filter(String query) {
        ArrayList<ProjectModel> filteredList = new ArrayList<>();

        for (ProjectModel property : propertyList) {
            if (property.getHeadline().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(property);
            }
        }

        propertyAdapter.filterList(filteredList);
    }

}