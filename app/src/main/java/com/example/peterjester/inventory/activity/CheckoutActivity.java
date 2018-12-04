package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;
import com.example.peterjester.inventory.model.entity.MapLocation;
import com.example.peterjester.inventory.recycler.RecyclerOnItemClickListener;
import com.example.peterjester.inventory.recycler.RecyclerVIewItemClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";
    public static final String TAG_RECYCLER = "RECYCLER_VIEW";

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items;

    private FusedLocationProviderClient mFusedLocationClient;
    private MapLocation currentGeolocation = null;
    final int userAgreePermissionCode = 1;

    private ItemPersistence persistenceProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("String", "calling the get last location");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        mRecyclerView = (RecyclerView) findViewById(R.id.viewAllView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


        buildRecyclerView();

    }

    /** Building the RecyclerView and RecyclerViewAdapter based on the dataset. **/
    private void buildRecyclerView(){
        Log.i(TAG_RECYCLER, "buildRecyclerView:called" );
        this.mRecyclerView = findViewById(R.id.viewAllView);

        persistenceProfile = new ItemPersistence();
        items = persistenceProfile.getDataFromDB();

        Location currentLoc = new Location("");
        currentLoc.setLatitude(Double.parseDouble(currentGeolocation.getLatitude()));
        currentLoc.setLongitude(Double.parseDouble(currentGeolocation.getLongitude()));
        Location itemLoc = new Location("");
        itemLoc.setLatitude(Double.parseDouble(items.get(0).getGeolocation().getLatitude()));
        itemLoc.setLongitude(Double.parseDouble(items.get(0).getGeolocation().getLongitude()));

        float distance = currentLoc.distanceTo(itemLoc);

        String test = String.format("%.02f", distance);
        Log.e("String", test);

        this.mAdapter = new ItemAdapter(items, this);

        persistenceProfile.addAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        this.mRecyclerView.setAdapter(mAdapter);


        // Creating a new listener for the RecyclerView
        mRecyclerView.addOnItemTouchListener(
                new RecyclerVIewItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerOnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
                        Item item = items.get(itemPosition);
                        Intent intent = new Intent(CheckoutActivity.this, ItemInfo.class);

                        // Pass the user info as paramater to the next activity.
                        intent.putExtra("ITEM", item);
                        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }

                })
        );
    }

    private void getCurrentLocation() {

        int currentPermission = ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        if( currentPermission!=PackageManager.PERMISSION_GRANTED ) ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, userAgreePermissionCode);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentGeolocation = new MapLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                        }
                    }
                });

        mFusedLocationClient.getLastLocation()
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // on fail
                    }
                });

    }


}
