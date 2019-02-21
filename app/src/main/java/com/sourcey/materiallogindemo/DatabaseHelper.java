package com.sourcey.materiallogindemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "boknings_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "username";
    private static final String COL4 = "datum";
    private static final String COL5 = "klassrum";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item, String username, String datum, String klassRum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, username);
        contentValues.put(COL3, item);
        contentValues.put(COL4, datum);
        contentValues.put(COL5, klassRum);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //Om fel set -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    //Hämta från databasen
    public Cursor getData(String userName) {
        Log.d("DatabaseHelper", "haha" + userName);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE TRIM(" + COL2 + ") = '" + userName.trim() + "'", null);

        //Cursor data = db.rawQuery(query, null);
        Log.d("DatabaseHelper", userName + " + " + data);

        return data;
    }

    //Hämta id av namnet (Kanske inte behövs än)
    public Cursor getItemID(int name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE ID = '" + name + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    //Uppdatera (kommer fixa funktionen i nästa kusr
    public void updateItem(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";

        db.execSQL(query);
    }


    //radera funktionen
    public void deleteName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: Raderade " + name + " från databasen.");
        db.execSQL(query);
    }

}
























