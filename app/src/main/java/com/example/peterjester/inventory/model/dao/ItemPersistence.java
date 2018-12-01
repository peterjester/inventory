package com.example.peterjester.inventory.model.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.peterjester.inventory.adapter.ItemAdapter;
import com.example.peterjester.inventory.model.entity.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ItemPersistence implements IPersistence {

//    public DatabaseAccess databaseAccess;

    ItemAdapter adapter;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    ArrayList<Item> items = new ArrayList<>();


    public ItemPersistence(){
    }

    public void addAdapter(ItemAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void insert(Object o) {

        // Cast the generic object to have access to the movie info.
        Item item = (Item) o;

        ref.child(item.getName()).setValue(item);

        /** Deprecated */
//        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();
//
//        // The ContentValues object create a map of values, where the columns are the keys
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ItemTable.COLUMN_NAME_NAME, item.getName());
//        contentValues.put(ItemTable.COLUMN_NAME_DESCRIPTION, item.getDescription());
//        contentValues.put(ItemTable.COLUMN_NAME_LOCATION, item.getLocation());
//        contentValues.put(ItemTable.COLUMN_NAME_PHOTOPATH, item.getPhotoPath());
//        contentValues.put(ItemTable.COLUMN_NAME_ID, item.getId());
//
//        // Insert the ContentValues into the Movie table.
//        sqLiteDatabase.insert(ItemTable.TABLE_NAME, null, contentValues);
//
//        sqLiteDatabase.close();
    }

    @Override
    public void delete(Object o) {

        Item item = (Item) o;

        // Define which column will be the parameter for deleting the record.
        String selection = ItemTable.COLUMN_NAME_NAME + "LIKE ? ";

        // Arguments must be identidied in the placehold order
//        String [] selectionArgs = { item.getName().trim() };

        // Get database instance
//        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();
//        sqLiteDatabase.delete(ItemTable.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void edit(Object o) {
        // TODO for the students to practice
    }

    @Override
    public ArrayList getDataFromDB() {

        // Create ArrayList of movies

        // Read from the database
        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Get the current location (based on the selected dataset available on firebase
                Item item = dataSnapshot.getValue(Item.class);

                items.add(item); // Adding a new element from the collection


                /** Once the task runs assynchronously, we need to notify the adapter of
                 any changes in the dataset, so it will automatically update the UI. **/
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


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

/**Deprecate */
        // Instatiate the database.
//        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

        // Gather all the records found for the user table.
//        Cursor cursor = sqLiteDatabase.rawQuery(ItemTable.select(), null);
//
//        // It will iterate since the first record gathered from the database.
//        cursor.moveToFirst();
//
//        // Check if there exist other records in the cursor
//        items = new ArrayList<>();
//
//        if(cursor != null && cursor.moveToFirst()){
//
//            do {
//                int id = cursor.getInt(cursor.getColumnIndex(ItemTable.COLUMN_NAME_ID));
//                String name = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_NAME));
//                String description = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_DESCRIPTION));
//                String location = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_LOCATION));
//                String photoPath = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_PHOTOPATH));
//
//                // Convert to Item object.
//                Item item = new Item(id, name, description, location, photoPath);
//                items.add(item);
//
//            } while (cursor.moveToNext()) ;
//        }

    }

    public ArrayList getDbMatchesForQuery(String queryString)
    {

        ref.orderByChild("name").startAt(queryString).endAt(queryString + "~").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Item item = dataSnapshot.getValue(Item.class);

                items.add(item); // Adding a new element from the collection

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


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

        /**Deprecated*/
        // Instantiate the database.
//        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

//        Cursor cursor = getWordMatches(query,null);
//
//        // It will iterate since the first record gathered from the database.
//        cursor.moveToFirst();
//
//        // Check if there exist other records in the cursor
//        items = new ArrayList<>();
//
//        if(cursor != null && cursor.moveToFirst()){
//
//            do {
//                int id = cursor.getInt(cursor.getColumnIndex(ItemTable.COLUMN_NAME_ID));
//                String name = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_NAME));
//                String description = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_DESCRIPTION));
//                String location = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_LOCATION));
//                String photoPath = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_PHOTOPATH));
//
//                // Convert to Item object.
//                Item item = new Item(id, name, description, location, photoPath);
//                items.add(item);
//
//            } while (cursor.moveToNext()) ;
//        }

    }


    public Cursor getWordMatches(String query, String[] columns) {
        String selection = ItemTable.COLUMN_NAME_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
//        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
//        builder.setTables(ItemTable.TABLE_NAME);
//
////        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();
//
//        Cursor cursor = builder.query(sqLiteDatabase,
//                columns, selection, selectionArgs, null, null, null);
//
//        if (cursor == null) {
//            return null;
//        } else if (!cursor.moveToFirst()) {
//            cursor.close();
//            return null;
//        }
        return null;
    }


}