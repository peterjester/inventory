package com.example.peterjester.inventory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;
import com.example.peterjester.inventory.recycler.RecyclerOnItemClickListener;
import com.example.peterjester.inventory.recycler.RecyclerVIewItemClickListener;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {

    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";
    public static final String TAG_RECYCLER = "RECYCLER_VIEW";

    private SearchView searchView = null;

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items;

    private ItemPersistence persistenceProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        searchView = findViewById(R.id.searchView);

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


    /** Building the RecyclerView and RecyclerViewAdapter based on the dataset. **/
    private void buildRecyclerView(){
        Log.i(TAG_RECYCLER, "buildRecyclerView:called" );
        this.mRecyclerView = findViewById(R.id.searchResultsView);

        persistenceProfile = new ItemPersistence();
        items = persistenceProfile.getDataFromDB();

        this.mAdapter = new ItemAdapter(items);

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
                        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG).show();
                    }

                })
        );
    }

}
