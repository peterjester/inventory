package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.peterjester.inventory.model.entity.Item;
import com.example.peterjester.inventory.model.entity.MapLocation;
import com.example.peterjester.inventory.recycler.RecyclerOnItemClickListener;
import com.example.peterjester.inventory.recycler.RecyclerVIewItemClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CheckoutActivity extends AppCompatActivity {

    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";
    public static final String TAG_RECYCLER = "RECYCLER_VIEW";

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items = new ArrayList<>();

    private FusedLocationProviderClient mFusedLocationClient;
    private MapLocation currentGeolocation = null;
    final int userAgreePermissionCode = 1;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = database.getReference();

    private boolean didLoadFirebase = false;
    private boolean didLoadLocation = false;

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

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
       // hello world
    }

    /** Building the RecyclerView and RecyclerViewAdapter based on the dataset. **/
    private void buildRecyclerView() {
        Log.i(TAG_RECYCLER, "buildRecyclerView:called");
        this.mRecyclerView = findViewById(R.id.viewAllView);

        firebaseLoadData();

        this.mAdapter = new ItemAdapter(items, this);

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
                            didLoadLocation = true;
                            buildRecyclerView();
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


    /**
     * @brief Had to ditch using the persistence profile and call the db here because
     *          the data was never returned in time.
     * @return
     */
    public ArrayList firebaseLoadData() {

        // Read from the database
        ref.child(auth.getUid()).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Get the current location (based on the selected dataset available on firebase
                Item item = dataSnapshot.getValue(Item.class);
                if(item.getGeolocation() != null) {
                    items.add(item); // Adding a new element from the collection
                    if(didLoadLocation) {
                        items = sortItemsLocations(items, currentGeolocation);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //hello
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("hello database", "onChildRemoved: ");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("hello database", "onChildRemoved: ");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("On cancelled", "Failed to read value.", error.toException());
            }

        });

        return items;
    }

    public static ArrayList<Item> sortItemsLocations(ArrayList<Item> locations, final MapLocation myLocation) {
        Comparator comp = new Comparator<Item>() {
            @Override
            public int compare(Item o, Item o2) {
                float[] result1 = new float[3];
                android.location.Location.distanceBetween(Double.parseDouble(myLocation.getLatitude()),
                        Double.parseDouble(myLocation.getLongitude()),
                        Double.parseDouble(o.getGeolocation().getLatitude()),
                        Double.parseDouble(o.getGeolocation().getLongitude()),
                        result1);
                Float distance1 = result1[0];

                float[] result2 = new float[3];
                android.location.Location.distanceBetween(Double.parseDouble(myLocation.getLatitude()),
                        Double.parseDouble(myLocation.getLongitude()),
                        Double.parseDouble(o2.getGeolocation().getLatitude()),
                        Double.parseDouble(o2.getGeolocation().getLongitude()),
                        result2);
                Float distance2 = result2[0];

                return distance1.compareTo(distance2);
            }
        };


        Collections.sort(locations, comp);
        return locations;
    }

}
