package com.example.peterjester.inventory.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.model.entity.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private ArrayList mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View listView;
        public MyViewHolder(View v) {
            super(v);
            listView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(ArrayList myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Item item = (Item) mDataset.get(position);

        TextView nameView = (TextView) holder.listView.findViewById(R.id.itemNameView);
        nameView.setText(item.getName());

        TextView descriptionView = (TextView) holder.listView.findViewById(R.id.descriptionView);
        descriptionView.setText(item.getDescription());

        TextView locationView = (TextView) holder.listView.findViewById(R.id.locationView);
        locationView.setText(item.getLocation());

        ImageView thumbnailView = holder.listView.findViewById(R.id.thumbnailImage);
        thumbnailView.setImageBitmap(BitmapFactory.decodeFile(item.getPhotoPath()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(ArrayList data) {
        mDataset = data;
        notifyDataSetChanged();
    }
}
