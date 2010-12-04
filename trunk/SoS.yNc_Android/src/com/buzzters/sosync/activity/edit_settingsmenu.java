package com.buzzters.sosync.activity;

import com.buzzters.sosync.activity.R;
import com.buzzters.sosync.dao.RulesDbAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class edit_settingsmenu extends Activity {
    /** Called when the activity is first created. */
	private RulesDbAdapter mDbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);
        mDbHelper = new RulesDbAdapter(this);
        mDbHelper.open();

        
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.events_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);        
        
              
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.action_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);        
        
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
        for(int i=1;i<7;i++)
        	mDbHelper.deleteRule(i);
        
        final Context ctxt = this;
        
        final Button button = (Button) findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String selectedgroup=new String();
            	CheckBox c1 = (CheckBox) findViewById(R.id.check1);
            	CheckBox c2 = (CheckBox) findViewById(R.id.check2);
            	CheckBox c3= (CheckBox) findViewById(R.id.check3);
            	if (c1.isChecked()) 
            	{
    				selectedgroup = c1.getText().toString();
            	}
            	else if(c2.isChecked())
            	{
            		selectedgroup = c2.getText().toString();
            	}
            	else if(c3.isChecked())
            	{
            		selectedgroup = c3.getText().toString();
            	}
            	System.out.println("selected group ->"+ selectedgroup);
            	
            	String event1=new String();
            	Spinner event = (Spinner) findViewById(R.id.spinner);
            	event1=event.getSelectedItem().toString();
            	System.out.println("selected event ->"+event1);

            	String action1=new String();
            	Spinner action = (Spinner) findViewById(R.id.spinner1);
            	action1=action.getSelectedItem().toString();
            	System.out.println("selected action ->"+action1);
            	
            	mDbHelper.createRule(selectedgroup,event1,action1);
            	
               Intent myIntent=new Intent(ctxt, com.buzzters.sosync.activity.display_rules.class);
               myIntent.putExtra("group",selectedgroup);
               myIntent.putExtra("event",event1);
               myIntent.putExtra("action",action1);
               startActivity(myIntent); 
            	// Perform action on click
            }
        });
                
    }
}


