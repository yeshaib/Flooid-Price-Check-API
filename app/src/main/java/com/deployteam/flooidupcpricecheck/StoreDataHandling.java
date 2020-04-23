package com.deployteam.flooidupcpricecheck;

import android.app.Activity;
import android.content.SharedPreferences;

public class StoreDataHandling extends Activity {


    public void SaveData (String SettingName, String PassData) {
        //Set Preference
        SharedPreferences myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor;
        prefsEditor = myPrefs.edit();
        prefsEditor.putString(SettingName, PassData);
        prefsEditor.commit();



    }

    public String GetData (String PassData) {
        //Get Preferenece
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);
        String StoredValue=myPrefs.getString("Settings_ServerIP", "");
        return StoredValue;
    }
}



