package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;

public class ItemInfo extends AppCompatActivity {

    private Button checkoutButton = null;
    private Button removeButton = null;
    private Button viewOnMapButton = null;

    private TextView nameView = null;
    private TextView descriptionView = null;
    private TextView locationView = null;

    private Item item = null;

    private ItemPersistence itemPersistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        itemPersistence = new ItemPersistence();

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("ITEM");

        setupButtonsAndViews();
        setupListeners();
    }

    private void setupButtonsAndViews() {
        checkoutButton = findViewById(R.id.checkoutButton);
        removeButton = findViewById(R.id.removeButton);
        viewOnMapButton = findViewById(R.id.viewOnMapButton);

        nameView = findViewById(R.id.itemTextView);
        descriptionView = findViewById(R.id.descriptionTextView);
        locationView = findViewById(R.id.locationTextView);

        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        locationView.setText(item.getLocation());
    }

    private void setupListeners() {
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoutItem();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem();
            }
        });

        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMap();
            }
        });
    }

    private void checkoutItem() {
        Toast.makeText(getApplicationContext(), "Checkout " + item.getName(), Toast.LENGTH_LONG).show();
        item.setCheckedOut();
        itemPersistence.insert(item);
    }

    private void removeItem() {
        itemPersistence.delete(item);
        Toast.makeText(getApplicationContext(), "Remove " + item.getName(), Toast.LENGTH_LONG).show();
        finish();
    }

    private void viewMap() {

        if(item.getGeolocation() != null) {

            // Navigating to MapActivity
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("LATITUDE", Double.parseDouble(item.getGeolocation().getLatitude()));
            intent.putExtra("LONGITUDE", Double.parseDouble(item.getGeolocation().getLongitude()));
            intent.putExtra("LOCATION", item.getLocation());

            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Location Available for " + item.getName(), Toast.LENGTH_LONG).show();
        }
    }

}
