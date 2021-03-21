package com.technek.parrotnight.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.technek.parrotnight.R;

/**
 * Created by SNOW DEN on 01/02/2018.
 */
public class ContentProvider {
    public SharedPreferences spLicences;
    public SharedPreferences.Editor licenceEditor;
    public SharedPreferences spUserDetails;
    public SharedPreferences.Editor userDetailsEditor;
    public SharedPreferences spFetchedData;
    public SharedPreferences.Editor fetchedDataEditor;

    public ContentProvider(Context context) {

        spLicences = context.getSharedPreferences("LICENCES", Context.MODE_PRIVATE);
        licenceEditor = spLicences.edit();
        spUserDetails = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        userDetailsEditor = spUserDetails.edit();
        spFetchedData = context.getSharedPreferences(context.getString(R.string.fetched_responses), Context.MODE_PRIVATE);
        fetchedDataEditor = spFetchedData.edit();

    }

}
