package com.deployteam.flooidupcpricecheck;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;

public class SettingsActivity extends Activity implements View.OnClickListener {
    private Utils Utils;
    private XMLHandling XMLHandling;
    private Object File;
    private StoreDataHandling StoreDataHandling;

    private String ServerIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get Layout Details
        setContentView(R.layout.activity_settins);
        // Get Buttons
        Button saveSettingsButton = (Button) findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(this); // calling onClick() method
        Button backToMainButton = (Button) findViewById(R.id.backToMainButton);
        backToMainButton.setOnClickListener(this);
        // Get Edit Text
        EditText serverIPTextView = (EditText) findViewById(R.id.serverIPEditText);

        // Shared Pref BaseLine
        SharedPreferences myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);

        // Get current Server IP
        myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);
        String ServerIP=myPrefs.getString("Settings_ServerIP", "");
        Utils.logd("Current Stored Value : " + ServerIP);
        if (ServerIP == "") {
            // Set Baseline IP
            SharedPreferences.Editor prefsEditor;
            prefsEditor = myPrefs.edit();
            prefsEditor.putString("Settings_ServerIP", "192.168.0.1");
            prefsEditor.commit();
            // Get updated IP
            ServerIP=myPrefs.getString("Settings_ServerIP", "");
            Utils.logd("After Create" + ServerIP);
            serverIPTextView.setText(ServerIP);

        } else {
            serverIPTextView.setText(ServerIP);
        }
        //File  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Settings.xml");
        Utils.logd("On Create Completed ");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.saveSettingsButton:
                // Hide Keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Get Text Field
                EditText serverIPTextView = (EditText) findViewById(R.id.serverIPEditText);

                // Get Text From Server IP
                String serverIP = serverIPTextView.getText().toString();
                Utils.logv("Text Server IP : " + serverIP);
                Utils.logv("Store Data");

                // Shared Pref Baseline
                SharedPreferences myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);

                // Get current Server IP
                myPrefs = getSharedPreferences("appSettings", MODE_PRIVATE);
                String CurrServerIP=myPrefs.getString("Settings_ServerIP", "");
                Utils.logd("Current Server IP : " + CurrServerIP);

                SharedPreferences.Editor prefsEditor;
                prefsEditor = myPrefs.edit();
                prefsEditor.putString("Settings_ServerIP", serverIP);
                prefsEditor.commit();

                Context context = getApplicationContext();
                CharSequence text = "Saved Settings!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;

            case R.id.backToMainButton:
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));

                break;


            default:
                break;

        }
    }
}
