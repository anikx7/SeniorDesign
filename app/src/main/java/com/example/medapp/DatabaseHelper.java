package com.example.medapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String db_name = "register.db";

    public static final String t_name = "users_table";
    public static final String col_1 = "ID";
    public static final String col_2 = "username";
    public static final String col_3 = "password";


    private static final String DB_Table = "med_table";  //table name
    private static final String ID = "ID";   //id
    private static final String NAME = "NAME";

    private static final String COL3 = "alarm_time";
    private static final String COL4 = "status";


    private static final String note_t = "note_table";  //table name
    private static final String ID1 = "ID";   //id
    private static final String note = "note";



    public static final String Create_Table= "Create Table "+ DB_Table +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT," +COL3 +" TEXT,"+COL4 +" TEXT)";



    public static final String Create_note= "Create Table "+ note_t +
            "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            note + " TEXT " + " )" ;


    public DatabaseHelper(Context context) {
        super (context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL ("CREATE TABLE users_table (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT )");
//db.execSQL (reg_t);
        db.execSQL (Create_Table);
        db.execSQL (Create_note);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL (" DROP TABLE IF EXISTS " + t_name);
        db.execSQL (" DROP TABLE IF EXISTS " + DB_Table  );
        db.execSQL (" DROP TABLE IF EXISTS " + note_t  );
        onCreate (db);
    }


    public boolean insertData (String name,String alarm_time1, String status1)              ///method to insert data into database
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put (NAME,name);       //one for one column
        contentValues.put (COL3,alarm_time1);
        contentValues.put (COL4,status1);
        long result = db.insert (DB_Table,null, contentValues);
        return result != -1;


    }
    public Cursor sr(String n) {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query =  "Select * from "+ DB_Table + " WHERE " + NAME+ " = '" + n + "'";
        Cursor cursor = db.rawQuery (query,null);
        return cursor;


    }


    public boolean insertNote(String nt)
    {

        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put (note,nt);       //one for one column
        long result = db.insert (note_t,null, contentValues);
        return result != -1;

    }

    public Cursor viewData()              ///method to view data
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query =  "Select * from " + DB_Table;
        Cursor cursor = db.rawQuery (query,null);
        return cursor;


    }

    public Cursor viewNotes()              ///method to view data
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query =  "Select * from " + note_t;
        Cursor cursor = db.rawQuery (query,null);
        return cursor;


    }




    public void deletedata(String n)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query = "DELETE FROM " + DB_Table + " WHERE " + NAME+ " = '" + n + "'";

        Log.d(TAG, "deletedata: Deleting " + n + " from database.");
        db.execSQL(query);

    }


    /////
    public void deletenote(String n)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query = "DELETE FROM " + note_t + " WHERE " + note+ " = '" + n + "'";

        Log.d(TAG, "deletedata: Deleting " + n + " from database.");
        db.execSQL(query);

    }





    public long adduser(String user, String password) {

        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put ("username", user);
        contentValues.put ("password", password);
        long res = db.insert (t_name, null, contentValues);
        db.close ();
        return res;
    }

    public boolean check(String username, String password) {
        String[] cols = {col_1};
        SQLiteDatabase db = getReadableDatabase ();
        String selection = col_2 + "=?" + " and " + col_3 + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query (t_name, cols, selection, selectionArgs, null, null, null);
        int count = cursor.getCount ();
        cursor.close ();
        db.close ();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }



    public Cursor medID(String n1)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query = "SELECT " + ID + " FROM " + DB_Table + " WHERE "
                + NAME + "= '" + n1 + "'" ;
        Cursor data = db.rawQuery (query,null);
        return data;

    }

    public Cursor noteID(String n1)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        String query = "SELECT " + ID1 + " FROM " + note_t + " WHERE "
                + note + "= '" + n1 + "'" ;
        Cursor data = db.rawQuery (query,null);
        return data;

    }







    public  boolean updatemed(String id, String n)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put (NAME,n);
        db.update (DB_Table, contentValues, "ID = ?", new String[] {id});
        return true;


    }


    public  boolean updatenote(String id, String n)
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues contentValues = new ContentValues ();
        contentValues.put (note,n);
        db.update (note_t, contentValues, "ID = ?", new String[] {id});
        return true;


    }



    //Alarms functions

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DB_Table;
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public void updateAlarm_Time(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + DB_Table + " SET " + COL3 +
                " = '" + newName + "' WHERE " + ID + " = '" + Integer.toString (id) + "'" +
                " AND " + COL3 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }
    public void updateAlarm_status(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + DB_Table + " SET " + COL4 +
                " = '" + newName + "' WHERE " + ID + " = '" + Integer.toString (id) + "'" +
                " AND " + COL4 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }


}