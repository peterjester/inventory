package com.example.peterjester.inventory.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {

    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";
    public static final String TAG_RECYCLER = "RECYCLER_VIEW";

    private SearchView searchView = null;

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items;

    //Firebase
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;

    private ItemPersistence persistenceProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        searchView = findViewById(R.id.searchView);

//        mRecyclerView = (RecyclerView) findViewById(R.id.searchResultsView);



        // specify an adapter (see also next example)
//        persistenceProfile = new ItemPersistence(this);
//        items = persistenceProfile.getDataFromDB();
//        mAdapter = new ItemAdapter(items);
//        mRecyclerView.setAdapter(mAdapter);

        getFirebaseData();

        buildRecyclerView();

        setupListeners();

    }

    private void setupListeners() {
        // perform set on query text listener event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // If the search box is empty, show all
                if(newText.matches("")){
                    items = persistenceProfile.getDataFromDB();
                }
                else {
                    items = persistenceProfile.getDbMatchesForQuery(newText);

                }
                mAdapter.updateList(items);
                return false;
            }
        });
    }


    /** Updating the RecyclerView based on the Firebase data.
     Step 1: make RecyclerViewAdapter and RecyclerView as global variables.
     Step 2: call the RecyclerViewAdapter.notifyDataSetChanged **/
    private void getFirebaseData() {

        items = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Get the current location (based on the selected dataset available on firebase
                Item item = dataSnapshot.getValue(Item.class);

                items.add(item); // Adding a new element from the collection


                /** Once the task runs assynchronously, we need to notify the adapter of
                 any changes in the dataset, so it will automatically update the UI. **/
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /** Building the RecyclerView and RecyclerViewAdapter based on the dataset. **/
    private void buildRecyclerView(){
        Log.i(TAG_RECYCLER, "buildRecyclerView:called" );
        this.mRecyclerView = findViewById(R.id.searchResultsView);
        this.mAdapter = new ItemAdapter(items);
        mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        this.mRecyclerView.setAdapter(mAdapter);


//        // Creating a new listener for the RecyclerView
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerVIewItemClickListener(getApplicationContext(), mRecyclerView, new RecyclerOnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//                        MapLocation item = mapLocations.get(itemPosition);
//                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//
//                        // TODO Create the intent for the broadcast.
//                        Intent intent = new Intent(MapLocationListActivity.this, MapActivity.class);
//                        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
//                        MapLocation item = mapLocations.get(itemPosition);
//                        intent.putExtra(TAG_SELECTED_ITEM, item);
//                        startActivity(intent);
//                    }
//                })
//        );
    }

}
