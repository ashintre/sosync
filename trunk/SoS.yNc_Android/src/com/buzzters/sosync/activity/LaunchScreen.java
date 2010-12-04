package com.buzzters.sosync.activity;

import com.buzzters.sosync.activity.R;
import com.buzzters.sosync.utility.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LaunchScreen extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(Constants.APP_TAG, "started creating activity");
        final Context ctxt = this;
        
        final Button editSettingsButton = (Button) findViewById(R.id.edit_settings);
        editSettingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Intent launchEditSettingsIntent = new Intent(ctxt, com.buzzters.sosync.activity.edit_settings.class);               
               startActivity(launchEditSettingsIntent); 
            }
        });
    }
}