package com.technek.parrotnight.models;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Subscriptiontimer extends Service {

    public Subscriptiontimer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        CharSequence text = "Subscription active!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
        onHandleIntent(intent);
        updatestimer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updatestimer() {

    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(500);
            timer();
            //Toast.makeText(this, "process running", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private long timer() throws ParseException {
        Context context = this;
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final long ONE_DAY = 24 * 60 * 60 * 1000;
        PackageInfo packageInfo;
        try {
            if (Build.VERSION.SDK_INT > 8/*Build.VERSION_CODES.FROYO*/) {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                Date installTime = new Date(packageInfo.firstInstallTime);
            } else {
                //firstinstalltime unsupported return last update time not first install time
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
                String sAppFile = appInfo.sourceDir;
                return new File(sAppFile).lastModified();
            }
        } catch (PackageManager.NameNotFoundException e) {
            //should never happen
            return 0;
        }
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String installTime = dateFormat.format(packageInfo.firstInstallTime);
        Log.d("NULL_T", "Installed: " + installTime);
        Date before = formatter.parse(installTime);
        Date now = new Date();
        long diff = now.getTime() - before.getTime();
        long days = diff / ONE_DAY;
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra("subscriptionend", days);
        //startActivity(intent);

       /* if (days > 60) {
            Log.d("NULL_T", "expired");
            String url = "www.primesoft.co.ke";
            Log.d("NULL_T", "days" + url);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("subscriptionend", days);
            startActivity(intent);

        } else {
            String url = "www.primesoft.co.ke";
            int daysremaining = (int) (30 - days);
            Log.d("NULL_T", "days" + daysremaining);
        }*/
        return 0;
    }
}
