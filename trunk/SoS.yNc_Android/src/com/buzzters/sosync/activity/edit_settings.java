package com.buzzters.sosync.activity;

import com.buzzters.sosync.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class edit_settings extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_startpage);
        
        final Context ctxt = this;
        
        final Button button = (Button) findViewById(R.id.create_rule);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               //System.out.println("in loop");
            	
               Intent myIntent=new Intent(ctxt, com.buzzters.sosync.activity.edit_settingsmenu.class);
               //myIntent.setClassName("com.buzzters.hotspotz.ui", "com.buzzters.hotspotz.ui.hotspotz1");
               //myIntent.setClassName("com.buzzters.hotspotz.ui", "hotspotz1");               
               startActivity(myIntent); 
            	// Perform action on click
            }
        });
        
    }
}