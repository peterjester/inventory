package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ItemInfo extends AppCompatActivity {

    private Button checkoutButton = null;
    private Button removeButton = null;
    private Button viewOnMapButton = null;

    private TextView nameView = null;
    private TextView descriptionView = null;
    private TextView locationView = null;

    private ImageView imageView = null;

    private Item item = null;

    private ItemPersistence itemPersistence;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference imageStorageReference = null;
    Bitmap photo = null;

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

        if(item.getPhotoPath() != null) {
            retrieveBitmapFromFirebaseDatabaseForItem(item);
            imageView = findViewById(R.id.imageInfoView);
            imageView.setImageBitmap(photo);
        }

        nameView.setText(item.getName());
        descriptionView.setText(item.getDescription());
        locationView.setText(item.getLocation());

        if(item.isCheckedOut()) {
            checkoutButton.setText(R.string.checkedInString);
            checkoutButton.setBackgroundColor(getResources().getColor(R.color.login));
        }
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

        String notice = item.isCheckedOut() ? "Checkin " : "Checkout ";

        Toast.makeText(getApplicationContext(), notice + item.getName(), Toast.LENGTH_LONG).show();
        item.setCheckedOut();
        itemPersistence.insert(item);
        finish();
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

    private void retrieveBitmapFromFirebaseDatabaseForItem(Item item) {

        if (item.getPhotoPath() != null) {
            imageStorageReference = storageReference.child("images/" + item.getPhotoPath());

            final long ONE_MEGABYTE = 1024 * 1024;
            imageStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }
    }
}


