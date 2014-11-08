package com.xtremeware.wanted;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Shakey on 12/8/13.
 */
public class ItemListDB {

    // Database Constants
    public static final String DB_NAME = "itemlist.db";
    public static final int    DB_VERSION = 1;

    // List Table Constants
    public static final String LIST_TABLE = "list";

    public static final String LIST_ID = "_id";
    public static final int    LIST_ID_COL = 0;

    public static final String LIST_NAME = "list_name";
    public static final int    LIST_NAME_COL = 1;

    // Item Table Constants
    public static final String ITEM_TABLE = "item";

    public static final String ITEM_ID = "_id";
    public static final int    ITEM_ID_COL = 0;

    public static final String ITEM_LIST_ID = "list_id";
    public static final int    ITEM_LIST_ID_COL = 1;

    public static final String ITEM_QUANTITY = "quantity";
    public static final int    ITEM_QUANTITY_COL = 2;

    public static final String ITEM_NAME = "item_name";
    public static final int    ITEM_NAME_COL = 3;

    public static final String ITEM_NOTES = "notes";
    public static final int    ITEM_NOTES_COL = 4;

    public static final String ITEM_SIZE = "size";
    public static final int    ITEM_SIZE_COL = 5;

    public static final String ITEM_DELETED = "date_deleted";
    public static final int    ITEM_DELETED_COL = 6;

    public static final String ITEM_HIDDEN = "hidden";
    public static final int    ITEM_HIDDEN_COL = 7;


    // Create and drop table statements
    public static final String CREATE_LIST_TABLE =
            "CREATE TABLE " + LIST_TABLE + " (" +
             LIST_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             LIST_NAME + " TEXT);";

    public static final String CREATE_ITEM_TABLE =
            "CREATE TABLE " + ITEM_TABLE + " (" +
             ITEM_ID        +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             ITEM_LIST_ID   +  " INTEGER, " +
             ITEM_QUANTITY  +  " TEXT, " +
             ITEM_NAME      +  " TEXT, " +
             ITEM_NOTES     +  " TEXT, " +
             ITEM_SIZE      +  " TEXT, " +
             ITEM_DELETED   +  " TEXT, " +
             ITEM_HIDDEN    +  " TEXT);";

    public static final String DROP_LIST_TABLE =
            "DROP TABLE IF EXISTS " + LIST_TABLE;

    public static final String DROP_ITEM_TABLE =
            "DROP TABLE IF EXISTS " + ITEM_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_LIST_TABLE);
            db.execSQL(CREATE_ITEM_TABLE);

            // Insert some default values into the lists table
            db.execSQL("INSERT INTO list VALUES (1, 'Personal')");
            db.execSQL("INSERT INTO list VALUES (2, 'Business')");


            // Insert some default values into the items table
            db.execSQL("INSERT INTO item VALUES (1, 1, '1', 'True Religion Jeans', 'This is dummy text, select checkmark to the" +
                    "right select the delete action bar icon to delete it', 'Small', '0', '0')");
            db.execSQL("INSERT INTO item VALUES (2, 1, '1', 'Timberland Boots', 'This is a what a normal database entry should look " +
                    "like', 'Large', '0', '0')");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Item list", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(ItemListDB.DROP_LIST_TABLE);
            db.execSQL(ItemListDB.DROP_ITEM_TABLE);
            onCreate(db);
        }
    }

    // Database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // Constructor
    public ItemListDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

    }

    // Private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    // Public methods
    public ArrayList<List> getLists() {
        ArrayList<List> lists = new ArrayList<List>();
        openReadableDB();
        Cursor cursor = db.query(LIST_TABLE, 
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
             List list = new List();
             list.setId(cursor.getInt(LIST_ID_COL));
             list.setName(cursor.getString(LIST_NAME_COL));
             
             lists.add(list);
        }
        cursor.close();
        closeDB();
        return lists;
    }

    public ArrayList<Item> getItems(String listName) {
        String where = ITEM_LIST_ID + "= ? AND " +
                ITEM_HIDDEN + "!='1'";
        long listID = getList(listName).getId();
        String[] whereArgs = { Long.toString(listID)};


        this.openReadableDB();
        Cursor cursor = db.query(ITEM_TABLE, null, where, whereArgs, null, null, ITEM_NAME + " ASC");
        ArrayList<Item> items = new ArrayList<Item>();
        while (cursor.moveToNext()) {
            items.add(getItemFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return items;
    }

    
    public List getList(String name) {
        String where = LIST_NAME + "= ?";
        String[] whereArgs = { name };

        openReadableDB();
        Cursor cursor = db.query(LIST_TABLE, null,
                where, whereArgs, null, null, null);
        List list = null;
        cursor.moveToFirst();
        list = new List(cursor.getInt(LIST_ID_COL),
                cursor.getString(LIST_NAME_COL));
        cursor.close();
        this.closeDB();

        return list;
    }



    public Item getItem(long id) {
        String where = ITEM_ID + "= ?";
        String[] whereArgs = { Long.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(ITEM_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Item item = getItemFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return item;

    }

    public List getList(long id) {
        String where = LIST_ID + "= ?";
        String[] whereArgs = { Long.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(LIST_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        List list = getListFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return list;

    }

    private static Item getItemFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                Item item = new Item (cursor.getInt(ITEM_ID_COL), cursor.getInt(ITEM_LIST_ID_COL), cursor.getString(ITEM_QUANTITY_COL),
                        cursor.getString(ITEM_NAME_COL), cursor.getString(ITEM_NOTES_COL), cursor.getString(ITEM_SIZE_COL),
                        cursor.getString(ITEM_DELETED_COL), cursor.getString(ITEM_HIDDEN_COL));
                return item;
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    private static List getListFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        else {
            try {
                List list = new List (cursor.getInt(LIST_ID_COL), cursor.getString(LIST_NAME_COL));
                return list;
            }
            catch (Exception e) {
                return null;
            }
        }
    }

    public long insertList(List list) {
    	ContentValues cv = new ContentValues();
    	cv.put(LIST_NAME, list.getName());
    	
    	this.openWriteableDB();
    	long rowID = db.insert(LIST_TABLE, null, cv);
    	this.closeDB();
    	
    	return rowID;
    }

    public long insertItem(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(ITEM_LIST_ID, item.getListId());
        cv.put(ITEM_QUANTITY, item.getQuantity());
        cv.put(ITEM_NAME, item.getName());
        cv.put(ITEM_NOTES, item.getNotes());
        cv.put(ITEM_SIZE, item.getSize());
        cv.put(ITEM_DELETED, item.getDeletedDate());
        cv.put(ITEM_HIDDEN, item.getHidden());

        this.openWriteableDB();
        long rowID = db.insert(ITEM_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateItem(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(ITEM_LIST_ID, item.getListId());
        cv.put(ITEM_QUANTITY, item.getQuantity());
        cv.put(ITEM_NAME, item.getName());
        cv.put(ITEM_NOTES, item.getNotes());
        cv.put(ITEM_SIZE, item.getSize());
        cv.put(ITEM_DELETED, item.getDeletedDate());
        cv.put(ITEM_HIDDEN, item.getHidden());

        String where = ITEM_ID + "= ?";
        String[] whereArgs = { String.valueOf(item.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(ITEM_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int updateList(List list) {
        ContentValues cv = new ContentValues();
        cv.put(LIST_NAME, list.getId());


        String where = LIST_ID + "= ?";
        String[] whereArgs = { String.valueOf(list.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(LIST_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteItem(long id) {
        String where = ITEM_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(ITEM_TABLE,where,whereArgs);
        this.closeDB();
        return rowCount;

    }

}
