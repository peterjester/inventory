package com.example.peterjester.inventory.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class ItemAdapter extends ArrayAdapter {

    public ItemAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
    }
}
