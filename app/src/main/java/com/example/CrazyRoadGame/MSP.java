package com.example.CrazyRoadGame;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {

    private static final String SP_FILE = "SharedPrefsFile";
    private SharedPreferences preferences;
    private static MSP msp;

    private MSP(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSP getInstance(Context context) {
        if (msp == null) {
            msp = new MSP(context);
        }
        return msp;
    }

    public String getStringSP(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putStringSP(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


}
