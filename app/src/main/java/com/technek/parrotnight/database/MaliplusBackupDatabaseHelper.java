package com.technek.parrotnight.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.technek.parrotnight.dbmodels.maliplusdb_note;
import com.technek.parrotnight.dbmodels.maliplusdbnotes;

public class MaliplusBackupDatabaseHelper extends SQLiteOpenHelper {
    private static final String BACKUP_DATABASE_NAME = "maliplusBackup.db";
    private static final int BACKUP_DB_VERSION = 7;
    public static final String TABLE_NAME = "item_master";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ITEMCODE = "itemcode";
    public static final String COLUMN_SALESPRICE = "salesprice";
    public static final String COLUMN_LEVIES = "taxlevies";
    public static final String COLUMN_TAXABLE = "taxable";
    public static final String COLUMN_SUOM = "suom";
    public static final String COLUMN_TAXRATE = "taxrate";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_ITEMNAME = "itemname";


    public MaliplusBackupDatabaseHelper(Context context) {
        super(context, BACKUP_DATABASE_NAME, null, BACKUP_DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(maliplusdb_note.CREATE_TABLE);
        db.execSQL( "CREATE TABLE " + TABLE_NAME + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "itemcode TEXT,location TEXT, salesprice TEXT, totalAmount TEXT, quantity TEXT," +
                "taxable TEXT, taxlevies TEXT, taxRate TEXT,suom TEXT"+")");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + maliplusdbnotes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + maliplusdb_note.TABLE_NAME);
        onCreate(db);
    }

}
