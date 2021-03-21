package com.technek.parrotnight.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;


import com.technek.parrotnight.models.TaxInfo;
import com.technek.parrotnight.models.TransactionsLedger;
import com.technek.parrotnight.util.Misc;
import com.technek.parrotnight.dbmodels.maliplusdbnotes;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {

    private static DatabaseAccess dbInstance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    Context context;
    public static final String HASH_PHRASE = "#PSL2018";
    public MaliplusDatabaseHelper instance;

    private DatabaseAccess(Context context) {
        openHelper = new MaliplusBackupDatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DatabaseAccess(context);
            SQLiteDatabase.loadLibs(context);
        }

        return dbInstance;
    }

    public void open() {

        database = SQLiteDatabase.openOrCreateDatabase("/data/data/com.primesoft.maliplus.myapplication/databases/maliplus.db", HASH_PHRASE, null);
       // database = instance.getWritableDatabase(HASH_PHRASE);

    }

    public void close() {
        database.close();
        if (database != null) {
            this.database.close();
        }
    }

    public TaxInfo getInfoByLocation(final String location) {
        open();
        TaxInfo info = null;

        String query = "select * from item_master where  location='"
                + location.replace("'", "''") + "'";
        Log.d("NULL_T", "" + query);
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            int id = cursor.getInt(0);
            String itemCode = cursor.getString(1);
            String salesPrice = cursor.getString(2);
            String levies = cursor.getString(3);
            String itemName = cursor.getString(4);
            String taxable = cursor.getString(5);
            double loyalty_redeem_value = cursor.getDouble(6);
            double loyalty_points_amount = cursor.getDouble(7);

            String SUOM = cursor.getString(8);
            String taxrate = cursor.getString(9);
            String loc = cursor.getString(10);


            info = new TaxInfo(id, itemCode, itemName, Misc.removeNull(salesPrice), Misc.removeNull(levies), Misc.removeNull(taxable),
                    loyalty_redeem_value,loyalty_points_amount,Misc.removeNull(taxrate), SUOM, loc);
        }
cursor.close();
        close();

        return info;
    }

    public TaxInfo getInfoByItemAndLocation(final String item, final String location) {
        open();
        TaxInfo info = new TaxInfo();

        String query = "select * from item_master where itemcode='" + item + "' and location='" + location + "'";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            int id = cursor.getInt(0);
            String itemCode = cursor.getString(1);
            String salesPrice = cursor.getString(2);
            String levies = cursor.getString(3);
            String itemName = cursor.getString(4);
            String taxable = cursor.getString(5);
            double loyalty_redeem_value = cursor.getDouble(6);
            double loyalty_points_amount = cursor.getDouble(7);
            String SUOM = cursor.getString(8);
            String taxrate = cursor.getString(9);
            String loc = cursor.getString(10);
            info = new TaxInfo(id, itemCode, itemName, Misc.removeNull(salesPrice), Misc.removeNull(levies), Misc.removeNull(taxable),
                    loyalty_redeem_value, loyalty_points_amount, Misc.removeNull(taxrate), SUOM, loc);
        }
        cursor.close();
close();
        return info;
    }

    public List<TransactionsLedger> getAllTransactions() {
        open();

        List<TransactionsLedger> allTransactions = new ArrayList<>();

        String query = "select * from " + TransactionsLedger.TABLE_NAME;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(0);
                String itemCode = cursor.getString(1);
                String location = cursor.getString(2);
                String salesPrice = cursor.getString(3);
                String totalAmount = cursor.getString(4);
                String quantity = cursor.getString(5);
                String taxable = cursor.getString(6);
                String levies = cursor.getString(7);
                String loyalty_awarded = cursor.getString(8);
                String taxrate = cursor.getString(9);
                String SUOM = cursor.getString(10);
                String timestamp = cursor.getString(11);
                String fiscal_month = cursor.getString(12);
                String fiscal_year= cursor.getString(13);
                String processedby= cursor.getString(14);
                String trnsync=cursor.getString(15);
String reference_number=cursor.getString(16);
String print_period=cursor.getString(17);

                TransactionsLedger trn = new TransactionsLedger(
                                id, itemCode, location,
                                Misc.removeNull(salesPrice), Misc.removeNull(totalAmount),
                                Misc.removeNull(quantity),loyalty_awarded, Misc.removeNull(taxable),
                                Misc.removeNull(levies),taxrate, SUOM,
                                timestamp, fiscal_month,
                                fiscal_year, processedby, trnsync, reference_number, print_period);

                allTransactions.add(trn);

                Log.d("INSERT", "FOUR: ");

                cursor.moveToNext();

            }
        }

        cursor.close();
close();
        return allTransactions;
    }

    public void insertTaxInfoData(String itemcode, double sale_price, double levies, double taxable, double taxrate, String suom, String location, String itemname) {
        open();
        String sql = "INSERT INTO item_master(itemcode, itemname, location, suom, salesprice, taxlevies, taxable, taxrate) values("
                + "'" + itemcode + "', "
                + "'" + itemname + "', "
                + "'" + location + "', "
                + "'" + suom + "', "
                + "'" + sale_price + "', "
                + "'" + levies + "', "
                + "'" + taxable + "', "
                + "'" + taxrate + "')";
        database.beginTransaction();
        database.execSQL(sql);
        database.setTransactionSuccessful();
        database.endTransaction();
        close();
    }

    public long insertTransactions(String itemcode, String location, double sale_price, double totalAmount, double quantity,double loyalty_awarded,
                                   double taxable, double levies, double taxRate, String suom,String timestamp,String fiscal_month,String fiscal_year,String processed_by,String trn_sync) {
        Context context;
        long rowId = -1;
        open();
        ContentValues values = new ContentValues();
        values.put("itemCode", itemcode);
        values.put("location", location);
        values.put("salesPrice", sale_price);
        values.put("totalAmount", totalAmount);
        values.put("quantity", quantity);
        values.put("loyalty_awarded", loyalty_awarded);
        values.put("taxable", taxable);
        values.put("taxlevies", levies);
        values.put("taxRate", taxRate);
        values.put("suom", suom);
        values.put("timestamp",timestamp);
        values.put("fiscal_month",fiscal_month);
        values.put("fiscal_year",fiscal_year);
        values.put("processed_by",processed_by);
        values.put("post_flag",trn_sync);

        rowId = database.insert(TransactionsLedger.TABLE_NAME, null, values);

        close();
//Log.d("NULL_T",""+rowId);
        return rowId;
       // return rowId+1;

    }
    public long insertCompanydata(String companyname, double loyalty_points, double loyalty_redeem_value) {


        open();        // SQLiteDatabase db=instance.getReadableDatabase(HASH_PHRASE);
        ContentValues values = new ContentValues();

        values.put("companyname", companyname);
        values.put("loyalty_redeem", loyalty_redeem_value);
        values.put("loyalty_points", loyalty_points);
        long result = database.insert(maliplusdbnotes.TABLE_NAME, null, values);
        Log.d("NULL_T", "" + result);
        close();

        return result;


    }


    public int checkUser(maliplusdbnotes maliplusdbnotes) {
        int id = -1;
        open();
        Cursor cursor = database.rawQuery("SELECT id FROM user WHERE name=? AND password=?", new String[]{maliplusdbnotes.getUsername(), maliplusdbnotes.getPassword()});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        cursor.close();

        close();
        return id;
    }

    public long updateEntry(String username, double password) {
        open();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        String where = "username = ?";
        database.update("customer_details", values, where, new String[]{username});
        long result = database.insert(maliplusdbnotes.TABLE_NAME, null, values);

        close();

        return result;
    }

    public long insertUserdata(String username, String fullname, String email, String password, String companyname, double phonenumber, String location, String firstimei, String secondimei) {
open();
        ContentValues values = new ContentValues();
        long result;
        values.put("username", username);
        values.put("fullname",fullname);
        values.put("email", email);
        values.put("password", password);
        values.put("phonenumber", phonenumber);
        values.put("location",location);
        values.put("companyname", companyname);
        values.put("firstimei", firstimei);
        values.put("secondimei", secondimei);
        Log.d("NULL_T","hilkjhjk");
         result = database.insert(maliplusdbnotes.TABLE_NAME, null, values);
     Log.d("NULL_T",""+result);
        close();

        return result;

    }

    private void openOrCreateDatabase() {

    }

    public void Backup() {
        open();
        String sql = "select * from Transactions";

        //String sql = "INSERT INTO maliplusBackup.Transactions(taxrate, location) SELECT taxrate, location FROM maliplus.Transactions";
//String sql="INSERT INTO maliplusBackup.Transactions SELECT * FROM maliplus.Transactions";
        Log.d("NULL_T", sql);
        database.beginTransaction();
        Log.d("NULL_T", "sql");
        database.execSQL(sql);
        Log.d("NULL_T", sql);
        database.setTransactionSuccessful();


        database.endTransaction();

        close();
    }

    public ArrayList<String> getLocations() {
        open();
        ArrayList<String> locations = new ArrayList<>();
        String query = "select * from locations";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String location = cursor.getString(cursor.getColumnIndex("location"));
                locations.add(location);

                cursor.moveToNext();
            }
        }
cursor.close();
        close();

        return locations;
    }

    public long insertlocations(String location) {
        open();
        ContentValues values = new ContentValues();
        values.put("location", location);

        long result = database.insert("locations", null, values);

        close();

        return result;
    }

    public void locationMatchEnforcer() {
        open();
        String locationsDelete = "delete from locations";
        String infoLocations = "select distinct location from item_master";

        database.execSQL(locationsDelete);

        Cursor cursor = database.rawQuery(infoLocations, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                ContentValues values = new ContentValues();

                values.put("location", cursor.getString(0));
                database.insert("locations", null, values);

                cursor.moveToNext();
            }
        }

        cursor.close();
        close();
    }

    public boolean checkDataExists() {
        open();
        Cursor cursor = database.rawQuery("select * from item_master ", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                Log.d("hello", "" + cursor);
                return true;
            }
            cursor.close();
        }
        cursor.close();
        close();
        return false;
    }


    public long controldb(String subscriptiontype, String syncstatus) {
        open();
        ContentValues values = new ContentValues();
        values.put("subscriptiontype", subscriptiontype);
        values.put("syncstatus", syncstatus);
        long result = database.insert("dbcontrol", null, values);

        close();

        return result;
    }
    public TransactionsLedger getCurrentTransaction(final long printId) {
        open();
        TransactionsLedger trn = new TransactionsLedger();
        String query = "select * from " + TransactionsLedger.TABLE_NAME + " where trnID ='" + printId + "'";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            //while (!cursor.isAfterLast()) {

            int id = cursor.getInt(0);
            String itemCode = cursor.getString(1);
            String location = cursor.getString(2);
            String salesPrice = cursor.getString(3);
            String totalAmount = cursor.getString(4);
            String quantity = cursor.getString(5);
            String taxable = cursor.getString(6);
            String levies = cursor.getString(7);
            String loyalty_awarded = cursor.getString(8);
            String taxrate = cursor.getString(9);
            String SUOM = cursor.getString(10);
            String timestamp = cursor.getString(11);
            String fiscal_month = cursor.getString(12);
            String fiscal_year= cursor.getString(13);
            String processedby= cursor.getString(14);
            String trnsync=cursor.getString(15);
            String reference_number=cursor.getString(16);
            String print_period=cursor.getString(17);

            trn = new TransactionsLedger(
                    id, itemCode, location,
                    Misc.removeNull(salesPrice), Misc.removeNull(totalAmount),
                    Misc.removeNull(quantity),loyalty_awarded, Misc.removeNull(taxable),
                    Misc.removeNull(levies),taxrate, SUOM,
                    timestamp, fiscal_month,
                    fiscal_year, processedby, trnsync, reference_number, print_period);
        }

        close();
        return trn;
    }

}
