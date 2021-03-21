package com.technek.parrotnight.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.database.sqlite.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.technek.parrotnight.R;
import com.technek.parrotnight.database.DatabaseAccess;
import com.technek.parrotnight.database.MaliplusDatabaseHelper;
import com.technek.parrotnight.models.TaxInfo;
import com.technek.parrotnight.models.TransactionsLedger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static SharedPreferences SP;
    private static final String DATABASE_NAME = "maliplus.db";
    private static final String BACKUP_DATABASE_NAME = "maliplusBackup.db";
    private MaliplusDatabaseHelper mydb;
    private Context context;
    public static final String HASH_PHRASE="#PSL2018";
    TaxInfo info;
    private NotificationManager mManager;
    public static final int CHANNEL_ID = 1;
    private DatabaseAccess dbaccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings);
        addPreferencesFromResource(R.xml.preferencesettings);
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String Stringnotification = SP.getString("", "");
        String Location = SP.getString("location", "Nkr");
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        context = this;
        mydb = new MaliplusDatabaseHelper(this);
        dbaccess = DatabaseAccess.getInstance(context);
        info = new TaxInfo();
        // disable checkbox
        setupSound();


        //       bindPreferenceSummaryToValue(findPreference("decimal"));
        //     bindPreferenceSummaryToValue(findPreference("perform_sync"));
        //   bindPreferenceSummaryToValue(findPreference("notification"));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // handle the preference change here
        if (key.equals("decimal")) {
            Preference decimalpref = findPreference(key);
            decimalpref.setSummary(sharedPreferences.getString(key, ""));
            Bundle bundle = new Bundle();
            bundle.putString("decimal", decimalpref + "");
            Intent intent = new Intent(settings.this, malimain.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (key.equals("perform_sync")) {
            String lastupdate="19/11/2018";
            mydb=new MaliplusDatabaseHelper(this);
            Preference syncpref = findPreference(key);
            sharedPreferences.getBoolean("perform_sync", false);
            AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
            builder.setTitle("Sync to Online Server");
            builder.setIcon(R.drawable.send);
            builder.setMessage("Sync with new prices!!\nLast update was made on" +lastupdate)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //synctoserver();
                          //  backup();
                            syncpref.setDefaultValue(false);
                            //syncpref.setEnabled(false);
                            if (CheckNetworkConnection()) {
                                updatenewprices();
                            }
                            else{
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            if (syncpref.equals("true")) {
                syncpref.setDefaultValue(false);
                //syncpref.setEnabled(false);
            }
        } else if (key.equals("export")) {
            Preference exportpref = findPreference(key);
            exportpref.setSummary(sharedPreferences.getString(key, ""));

            File sd = Environment.getExternalStorageDirectory();
            String csvFile = "myData.xls";

            File directory = new File(sd.getAbsolutePath());
            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }
            //file path
            File file = new File(directory, csvFile);
            exportdatabase();
        } else if (key.equals("updateprices")) {
            Preference notifypref = findPreference(key);
            notifypref.setSummary(sharedPreferences.getString(key, String.valueOf(true)));
        } else if (key.equals("feedbackmessage")) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download the app on www.primesoft.co.ke or contact 0720789605\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Your ultimate software solutions provider");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
    else if(key.equals("terms")){
            Intent intent=new Intent(settings.this,termsofuse.class);
            startActivity(intent);
        }
    }
    private void updatenewprices() {
        SQLiteDatabase db = mydb.getReadableDatabase(HASH_PHRASE);
        db.execSQL("DELETE FROM item_master"); //delete all rows in a table
        db.close();
        Intent intent=new Intent(settings.this,MaliplusUpdater.class);
        startActivity(intent);
    }
    private void synctoserver() {
        Log.d("NULL_T", "start fetching  server");
        String url = "http://139.162.208.37:8080/all";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Type taxListType = new TypeToken<ArrayList<TaxInfo>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<TaxInfo> locationPrices = gson.fromJson(response, taxListType);

                    if (locationPrices.size() > 0) {
                        for (TaxInfo info : locationPrices) {
                            dbaccess.insertTaxInfoData(
                                    info.getItemCode(), info.getUnitPrice(), info.getLevies(), info.getTaxable(), info.getTaxRate(),
                                    info.getSUOM(), info.getLocation(), info.getItemName());
                            Log.d("NULL_T", "fetched from server"+locationPrices);
                        }
                    }
                }, error -> {
            Log.d("NULL_T", error.getMessage());

        }) {
            @Override
            protected Map<String, String> getParams() {         // Adding parameters
                Map<String, String> params = new HashMap<String, String>();
                return params; }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);
    }
    private void exportdatabase() {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Documents/maliplus");
        dir.mkdirs();

        String FILENAME = "maliplusfueltransactions.csv";
        File file = new File(dir, FILENAME);
        List<TransactionsLedger> lst = dbaccess.getAllTransactions();

        for (TransactionsLedger t : lst) {
            Log.d("NULL_T", t.toString());
            String entry = t.toString();
            Log.d("NULL_T", "save to csv");
            try {
                String path = file.getAbsolutePath();
                //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";
                if (!dir.exists()) {
                    dir.mkdir();
                }
                FileOutputStream output = openFileOutput(String.valueOf(file), Context.MODE_APPEND);
                FileOutputStream outputStream = openFileOutput(FILENAME, Context.MODE_APPEND);
                FileOutputStream outputStream1 = new FileOutputStream(file);
                FileOutputStream fos;
                fos=new FileOutputStream(file);
                //BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos));
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));
                for (int i=0;i<lst.size();i++) {
                    bw.write(String.valueOf(entry.getBytes()));
                    bw.newLine(); }
                    bw.close();
                //outputStream.write(entry.getBytes());
                outputStream1.write(entry.getBytes());
                output.write(entry.getBytes());
                outputStream.close();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
    }
    private boolean backup() {
        /*File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "data/data/com.example.newton.myapplication/databases/" + DATABASE_NAME;
        String backupDBPath = "/data/data/com.example.newton.myapplication/databases/" + BACKUP_DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MaliplusBackupDatabaseHelper maliplusBackupDatabaseHelper = new MaliplusBackupDatabaseHelper(this);
        Log.d("NULL_T", "backupsync");
        dbaccess.Backup();*/
        return true; }
    private boolean CheckNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null & networkInfo.isConnected());
    }
    private void setupSound() {

    }
    private void setupCustomizeColors() {

        }
}