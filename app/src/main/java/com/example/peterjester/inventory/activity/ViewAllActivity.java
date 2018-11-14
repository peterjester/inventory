package com.example.peterjester.inventory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.dao.ItemPersistence;
import com.example.peterjester.inventory.model.entity.Item;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        mRecyclerView = (RecyclerView) findViewById(R.id.viewAllView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ItemPersistence persistenceProfile = new ItemPersistence(this);
        items = persistenceProfile.getDataFromDB();
        mAdapter = new ItemAdapter(items);
        mRecyclerView.setAdapter(mAdapter);


    }
}
