package com.example.peterjester.inventory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.peterjester.inventory.R;

public class SearchItemActivity extends AppCompatActivity {

    private SearchView searchView = null;
    private TextView resultsText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        searchView = findViewById(R.id.searchView);
        resultsText = findViewById(R.id.resultsText);

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
                // @todo add in adapter, and filter data based on text input
                resultsText.setText(newText);
                return false;
            }
        });
    }
}
