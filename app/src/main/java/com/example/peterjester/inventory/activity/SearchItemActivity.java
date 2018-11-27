package com.example.peterjester.inventory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {

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

        mRecyclerView = (RecyclerView) findViewById(R.id.searchResultsView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        persistenceProfile = new ItemPersistence(this);
        items = persistenceProfile.getDataFromDB();
        mAdapter = new ItemAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

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
                items = persistenceProfile.getDbMatchesForQuery(newText);
                mAdapter.updateList(items);
                return false;
            }
        });
    }
}
