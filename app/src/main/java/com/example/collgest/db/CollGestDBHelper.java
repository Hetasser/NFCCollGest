package com.example.collgest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CollGestDBHelper extends SQLiteOpenHelper {


//    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "CollGest_Manager";

    // Table name: GestItem.
    private static final String TABLE_ITEM = "GestItem";

    private static final String COLUMN_ITEM_GUID = "itemGUID";
    private static final String COLUMN_ITEM_NAME = "itemName";
    private static final String COLUMN_ITEM_MINPLAYERS = "itemMinJoueurs";
    private static final String COLUMN_ITEM_MAXPLAYERS = "itemMaxJoueurs";
    private static final String COLUMN_ITEM_DURATION = "itemDuration";
    private static final String COLUMN_ITEM_TYPES = "itemTypes";
    private static final String COLUMN_ITEM_LASTPLAYED = "itemLastPlayed";
    private static final String COLUMN_ITEM_CHECKEDOUT = "itemCheckedOut";


    public CollGestDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script.
        String script = "CREATE TABLE " + TABLE_ITEM + "("
                + COLUMN_ITEM_GUID + " TEXT PRIMARY KEY," + COLUMN_ITEM_NAME + " TEXT,"
                + COLUMN_ITEM_MINPLAYERS + " INTEGER," + COLUMN_ITEM_MAXPLAYERS + " INTEGER,"
                + COLUMN_ITEM_DURATION + " INTEGER," + COLUMN_ITEM_TYPES + " TEXT,"
                + COLUMN_ITEM_LASTPLAYED + " TEXT," + COLUMN_ITEM_CHECKEDOUT + " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
        addGestItem(new CollGestItem("E9DCF61F-91C2-46F1-8AE6-B0E70F04552F", "Tribo", 2, 2, 10, "Echecs", "2021-05-31", ""), db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("oldVersion : " + oldVersion + " / newVersion : " + newVersion);
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);

        // Create tables again
        onCreate(db);


    }

    public void addGestItem(CollGestItem collGestItem, SQLiteDatabase db) {


        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_GUID, collGestItem.getItemGUID());
        values.put(COLUMN_ITEM_NAME, collGestItem.getItemName());
        values.put(COLUMN_ITEM_MINPLAYERS, collGestItem.getItemMinJoueurs());
        values.put(COLUMN_ITEM_MAXPLAYERS, collGestItem.getItemMaxJoueurs());
        values.put(COLUMN_ITEM_DURATION, collGestItem.getItemDuration());
        values.put(COLUMN_ITEM_TYPES, collGestItem.getItemTypes());
        values.put(COLUMN_ITEM_LASTPLAYED, collGestItem.getItemLastPlayed());
        values.put(COLUMN_ITEM_CHECKEDOUT, collGestItem.getItemCheckedOut());

        // Inserting Row
        db.insert(TABLE_ITEM, null, values);

        // Closing database connection
        //db.close();
    }


    public CollGestItem getCollGestItemFromDB(String CollGestItemGUID) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEM, new String[]{COLUMN_ITEM_GUID,
                        COLUMN_ITEM_NAME, COLUMN_ITEM_MINPLAYERS, COLUMN_ITEM_MAXPLAYERS, COLUMN_ITEM_DURATION, COLUMN_ITEM_TYPES, COLUMN_ITEM_LASTPLAYED, COLUMN_ITEM_CHECKEDOUT},
                COLUMN_ITEM_GUID + "=?",
                new String[]{String.valueOf(CollGestItemGUID)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            CollGestItem collGestItem = new CollGestItem(
                    cursor.getString(0),
                    cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)),
                    Integer.parseInt(cursor.getString(4)),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            );
            cursor.close();
            return collGestItem;
        }
        // return GestItem
        return null;
    }


    public List<CollGestItem> getAllGestItem(SQLiteDatabase db) {

        List<CollGestItem> gestItemList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM;

        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CollGestItem collGestItem = new CollGestItem(
                        cursor.getString(0),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getColumnName(7)
                );
                // Adding GestItem to list
                gestItemList.add(collGestItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return GestItem list
        return gestItemList;
    }

    public int getGestItemsCount() {

        String countQuery = "SELECT  * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public void updateGestItem(CollGestItem collGestItem) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_GUID, collGestItem.getItemGUID());
            values.put(COLUMN_ITEM_NAME, collGestItem.getItemName());
            values.put(COLUMN_ITEM_MINPLAYERS, collGestItem.getItemMinJoueurs());
            values.put(COLUMN_ITEM_MAXPLAYERS, collGestItem.getItemMaxJoueurs());
            values.put(COLUMN_ITEM_DURATION, collGestItem.getItemDuration());
            values.put(COLUMN_ITEM_TYPES, collGestItem.getItemTypes());
            values.put(COLUMN_ITEM_LASTPLAYED, collGestItem.getItemLastPlayed());
            values.put(COLUMN_ITEM_CHECKEDOUT, collGestItem.getItemCheckedOut());

            // updating row
            db.update(TABLE_ITEM, values, COLUMN_ITEM_GUID + " = ?",
                    new String[]{String.valueOf(collGestItem.getItemGUID())});
        }
        System.out.println("/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/");
        System.out.println(collGestItem.toString());
        System.out.println("/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/+/");
    }

    public void deleteGestItem(CollGestItem collGestItem) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM, COLUMN_ITEM_GUID + " = ?",
                new String[]{String.valueOf(collGestItem.getItemGUID())});
        //db.close();
    }
}
