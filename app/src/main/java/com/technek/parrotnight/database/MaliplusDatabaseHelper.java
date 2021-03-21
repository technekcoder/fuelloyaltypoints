package com.technek.parrotnight.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.util.Log;

import com.technek.parrotnight.models.listcontrol;
import com.technek.parrotnight.models.locations;
import com.technek.parrotnight.dbmodels.maliplusdb_note;
import com.technek.parrotnight.dbmodels.maliplusdbnotes;
import com.technek.parrotnight.models.TransactionsLedger;

public class MaliplusDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maliplus.db";
    private static final String BACKUP_DATABASE_NAME = "maliplusBackup.db";
    private static final int DATABASE_VERSION = 7;
    public static boolean enableSQLCypher = true;
    public static final String HASH_PHRASE = "#PSL2018";

    public MaliplusDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(maliplusdb_note.CREATE_TABLE);
        db.execSQL(maliplusdbnotes.CREATE_TABLE_IF_NOT_EXISTS);
        db.execSQL(TransactionsLedger.CREATE_TABLE);
        db.execSQL(locations.CREATE_TABLE_IF_NOT_EXISTS);
        db.execSQL(listcontrol.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + maliplusdbnotes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + maliplusdb_note.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TransactionsLedger.TABLE_NAME);
        onCreate(db);
        throw new RuntimeException("error");
    }

    /*    SQLiteDatabase getReadableDatabase() {
            return(super.getReadableDatabase(HASH_PHRASE));
        }

        SQLiteDatabase getWritableDatabase() {
            return(super.getWritableDatabase(HASH_PHRASE));
        }
    */
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase(HASH_PHRASE);
        // SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM  " + maliplusdbnotes.TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public long insertUserdata(String username, String fullname, String email, String password, String companyname, double phonenumber, String location, String firstimei, String secondimei) {


        SQLiteDatabase db = this.getWritableDatabase(HASH_PHRASE);
        // SQLiteDatabase db=instance.getReadableDatabase(HASH_PHRASE);
        ContentValues values = new ContentValues();
        long result=-1;
        values.put("username", username);
        values.put("fullname", fullname);
        values.put("email", email);
        values.put("password", password);
        values.put("phonenumber", phonenumber);
        values.put("location", location);
        values.put("companyname", companyname);
        values.put("firstimei", firstimei);
        values.put("secondimei", secondimei);
        Log.d("NULL_T", "hilkjhjk");
        result = db.insert(maliplusdbnotes.TABLE_NAME, null, values);
        Log.d("NULL_T", "" + result);
        close();

        return result;


    }



    /*public boolean checkDataExists() {
        SQLiteDatabase db = this.getReadableDatabase(HASH_PHRASE);
        Cursor cursor = db.rawQuery("select * from item_master ", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Log.d("hello", "" + cursor);
                return true;
            }
            cursor.close();
        }
        boolean result = false;
        close();
        return result;
    }*/
}

