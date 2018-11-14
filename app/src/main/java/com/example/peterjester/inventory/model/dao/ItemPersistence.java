package com.example.peterjester.inventory.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.peterjester.inventory.model.entity.Item;

import java.util.ArrayList;

public class ItemPersistence implements IPersistence {

    public DatabaseAccess databaseAccess;

    public ItemPersistence(Context context){
        this.databaseAccess = new DatabaseAccess(context);
    }

    @Override
    public void insert(Object o) {

        // Cast the generic object to have access to the movie info.
        Item item = (Item) o;

        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

        // The ContentValues object create a map of values, where the columns are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemTable.COLUMN_NAME_NAME, item.getName());
        contentValues.put(ItemTable.COLUMN_NAME_DESCRIPTION, item.getDescription());
        contentValues.put(ItemTable.COLUMN_NAME_LOCATION, item.getLocation());
        contentValues.put(ItemTable.COLUMN_NAME_ID, item.getId());

        // Insert the ContentValues into the Movie table.
        sqLiteDatabase.insert(ItemTable.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    @Override
    public void delete(Object o) {

        Item item = (Item) o;

        // Define which column will be the parameter for deleting the record.
        String selection = ItemTable.COLUMN_NAME_NAME + "LIKE ? ";

        // Arguments must be identidied in the placehold order
//        String [] selectionArgs = { item.getName().trim() };

        // Get database instance
        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();
//        sqLiteDatabase.delete(ItemTable.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void edit(Object o) {
        // TODO for the students to practice
    }

    @Override
    public ArrayList getDataFromDB() {

        // Create ArrayList of movies
        ArrayList<Item> items = null;

        // Instatiate the database.
        SQLiteDatabase sqLiteDatabase = databaseAccess.getWritableDatabase();

        // Gather all the records found for the user table.
        Cursor cursor = sqLiteDatabase.rawQuery(ItemTable.select(), null);

        // It will iterate since the first record gathered from the database.
        cursor.moveToFirst();

        // Check if there exist other records in the cursor
        items = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){

            do {
                int id = cursor.getInt(cursor.getColumnIndex(ItemTable.COLUMN_NAME_ID));
                String name = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_NAME));
                String description = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndex(ItemTable.COLUMN_NAME_LOCATION));

                // Convert to Item object.
                Item item = new Item(id, name, description, location, null);
                items.add(item);

            } while (cursor.moveToNext()) ;
        }

        return items;
    }
}