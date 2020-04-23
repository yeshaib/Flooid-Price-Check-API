package com.deployteam.flooidupcpricecheck;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity implements View.OnClickListener {

    private String AppSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedpreferences;
        // Get App Version
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        // Get Buttons
        Button getItemPriceButton = (Button) findViewById(R.id.getItemPriceButton);
        getItemPriceButton.setOnClickListener(this); // calling onClick() method
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);

        // Set App Version
        TextView appVersion = (TextView) findViewById(R.id.appVersionTextView);
        appVersion.setText("App Ver : " + versionCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.getItemPriceButton:
                // Hide Keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                final String TAG = "";
                // Get Fields
                EditText itemUPCEditText = (EditText) findViewById(R.id.enterItemUPCEditText);
                TextView resultItemPrice = (TextView) findViewById(R.id.itemPriceTextView);
                TextView resultItemName = (TextView) findViewById(R.id.itemNameTextView);
                TextView resultItemCode = (TextView) findViewById(R.id.itemCodeTextView);

                //Get Item UPC
                int intItemUPC = Integer.parseInt(itemUPCEditText.getText().toString());
                String itemUPC = Integer.toString(intItemUPC);

                //Get Server IP from Settings
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


                } else {
                    ServerIP=myPrefs.getString("Settings_ServerIP", "");
                }

                try {
                    //String[] flooidAPIres = new flooidAPIAST().execute(itemUPC).get();
                    String[] flooidAPIres = new FlooidAPI.flooidAPIitem().execute(itemUPC, ServerIP).get();
                    Log.d(TAG, "onClick() called with: v = [" + flooidAPIres[0] + "]");
                    Log.d(TAG, "onClick() called with: v = [" + flooidAPIres[1] + "]");
                    Log.d(TAG, "onClick() called with: v = [" + flooidAPIres[2] + "]");
                    Utils.logd("onClick() called with: v = [" + flooidAPIres[3] + "]");
                    Utils.logd("onClick() called with: v = [" + flooidAPIres[4] + "]");
                    resultItemName.setText(flooidAPIres[0]);
                    resultItemPrice.setText("$" + flooidAPIres[1]);
                    resultItemCode.setText(flooidAPIres[2]);


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.settingsButton:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                break;


            default:
                break;

        }
    }
}