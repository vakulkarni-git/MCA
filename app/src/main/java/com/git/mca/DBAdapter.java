package com.git.mca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public static final String DB_NAME = "MyDB";
    public static final String TABLE_NAME = "contacts";
    public static final int DATABASE_VERSION = 1;

    private String CREATE_DATABASE = "create table contacts (_id integer primary key autoincrement," +
            "name text not null, email text not null)";
    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public DBAdapter(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super (context, DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_DATABASE);
            } catch (Exception e) {
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertContact(String name, String email) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);

        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllContacts() {
        return db.query(TABLE_NAME, new String [] {KEY_ID, KEY_NAME, KEY_EMAIL}, null,
        null, null, null, null);
    }
}
