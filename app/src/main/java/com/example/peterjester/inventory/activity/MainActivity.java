package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peterjester.inventory.R;

public class MainActivity extends AppCompatActivity {

    private Button searchButton = null;
    private Button addItemButton = null;
    private Button beaconButton = null;
    private Button viewNearbyButton = null;
    private Button logoutButton = null;
    private Button viewAllButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchButton);
        addItemButton = findViewById(R.id.addItemButton);
        beaconButton = findViewById(R.id.configureBeaconButton);
        viewNearbyButton = findViewById(R.id.viewNearbyItemsButton);
        logoutButton = findViewById(R.id.logoutButton);
        viewAllButton = findViewById(R.id.viewAllItems);

        setupListeners();
    }

    private void setupListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, query_items.class));
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddItemActivity.class));
            }
        });

        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CheckoutActivity.class));
            }
        });

        viewNearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CheckoutActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewAllActivity.class));
            }
        });
    }
}
