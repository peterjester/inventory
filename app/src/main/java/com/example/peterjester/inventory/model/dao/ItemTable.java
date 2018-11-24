package com.example.peterjester.inventory.model.dao;

/**
 * @author peterjester
 */
public class ItemTable {

    /**
     * @todo Add in tags
     * @todo how do we store images?
     */

    /** Defining the Table Content **/
    public static final String TABLE_NAME = "item";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_LOCATION = "location";
    public static final String COLUMN_NAME_PHOTOPATH = "photoPath";


    public static String create(){
        return new String ( "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_DESCRIPTION  + " TEXT," +
                COLUMN_NAME_LOCATION + " TEXT," +
                COLUMN_NAME_PHOTOPATH + " TEXT)" );
    }

    public static String createVirtual(){
        return new String ( "CREATE VIRTUAL TABLE " + TABLE_NAME +
                " USING fts3 (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_DESCRIPTION  + " TEXT," +
                COLUMN_NAME_LOCATION + " TEXT," +
                COLUMN_NAME_PHOTOPATH + " TEXT)" );
    }

    public static String select(){
        return new String("SELECT * FROM " + TABLE_NAME);

    }

    public static final String delete(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}




