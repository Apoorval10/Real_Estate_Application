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
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<ProjectModel> propertyList;
    private ProjectAdapter propertyAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
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
        propertyAdapter = new ProjectAdapter(MainActivity.this, propertyList,phone);

        String phoneNumber = getIntent().getStringExtra("phone");
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

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot propertiesSnapshot = userSnapshot.child("properties");

                    for (DataSnapshot propertySnapshot : propertiesSnapshot.getChildren()) {
                        ProjectModel property = propertySnapshot.getValue(ProjectModel.class);
                        propertyList.add(property);
                    }
                }

                propertyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(recyclerView, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String phoneNumber = getIntent().getStringExtra("phone");

        // Handle menu item clicks here
        switch (item.getItemId()) {
            case R.id.menu_upload:
                Intent intent = new Intent(MainActivity.this, UploadProductActivity.class);
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                logoutUser();
                return true;
            case R.id.menu_profile:
                Intent i = new Intent(MainActivity.this, profile.class);
                i.putExtra("phone", phoneNumber);
                startActivity(i);
                return true;
            case R.id.menu_myProperty:
                Intent ii = new Intent(MainActivity.this, myProperties.class);
                ii.putExtra("phone", phoneNumber);
                startActivity(ii);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, login_activity.class));
        finish();
    }
    private void filter(String query) {
        ArrayList<ProjectModel> filteredList = new ArrayList<>();

        for (ProjectModel property : propertyList) {
            String propertyTitle = property.getPropertyTitle();
            if (propertyTitle != null && propertyTitle.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(property);
            }
        }

        propertyAdapter.filterList(filteredList);
    }


}
