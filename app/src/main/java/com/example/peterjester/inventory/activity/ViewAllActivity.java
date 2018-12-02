package com.example.peterjester.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.peterjester.inventory.recycler.RecyclerOnItemClickListener;
import com.example.peterjester.inventory.recycler.RecyclerVIewItemClickListener;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {

    public static final String TAG_SELECTED_ITEM = "SELECTED_ITEM";
    public static final String TAG_RECYCLER = "RECYCLER_VIEW";

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Item> items;

    private ItemPersistence persistenceProfile = null;

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

        buildRecyclerView();

    }

    /** Building the RecyclerView and RecyclerViewAdapter based on the dataset. **/
    private void buildRecyclerView(){
        Log.i(TAG_RECYCLER, "buildRecyclerView:called" );
        this.mRecyclerView = findViewById(R.id.viewAllView);

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
                        Intent intent = new Intent(ViewAllActivity.this, ItemInfo.class);
                        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }

                })
        );
    }

}
