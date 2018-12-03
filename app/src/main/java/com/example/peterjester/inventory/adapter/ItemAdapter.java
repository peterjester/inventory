package com.example.peterjester.inventory.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peterjester.inventory.R;
import com.example.peterjester.inventory.model.entity.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList mDataset;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference imageStorageReference = null;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(ArrayList myDataset) {
        mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // Creating inner class as ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        // Elements defined in the UI
        CardView parentLayout;

        // For now, we don't need the imageView.
        // The images will be customized later on.
        ImageView thumbnailView = null;
        Bitmap photo = null;

        TextView locationView = null;
        TextView nameView = null;
        TextView descriptionView = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.cardView);
            thumbnailView = itemView.findViewById(R.id.thumbnailImage);
            locationView = itemView.findViewById(R.id.locationView);
            nameView = itemView.findViewById(R.id.itemNameView);
            descriptionView = itemView.findViewById(R.id.descriptionView);
        }

        private void retrieveBitmapFromFirebaseDatabaseForItem(Item item) {
            imageStorageReference = storageReference.child("images/" + item.getPhotoPath());

            final long ONE_MEGABYTE = 1024 * 1024;
            imageStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    thumbnailView.setImageBitmap(photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }
    }

    // Methods inherited from ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // The method implementaiton might change based on the root layout.
    // Everytime a new item is inflated on the view it will be called.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Item item = (Item) mDataset.get(position);

        viewHolder.descriptionView.setText(item.getDescription());
        viewHolder.nameView.setText(item.getName());
        viewHolder.locationView.setText(item.getLocation());
        viewHolder.retrieveBitmapFromFirebaseDatabaseForItem(item);
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
