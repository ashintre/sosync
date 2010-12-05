package com.buzzters.sosync.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.buzzters.sosync.dao.RulesDbAdapter;
import com.buzzters.sosync.utility.Constants;

public class edit_settingsmenu extends Activity {
    /** Called when the activity is first created. */
	private RulesDbAdapter mDbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_menu);
        mDbHelper = new RulesDbAdapter(this);
        mDbHelper.open();

        
        Spinner eventSelectionSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> eventsArray = ArrayAdapter.createFromResource(
                this, R.array.events_array, android.R.layout.simple_spinner_item);
        eventsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSelectionSpinner.setAdapter(eventsArray);        
        
              
        Spinner actionSelectionSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> actionsArray = ArrayAdapter.createFromResource(
                this, R.array.action_array, android.R.layout.simple_spinner_item);
        actionsArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSelectionSpinner.setAdapter(actionsArray);        
        
        Spinner prioritySelectionSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> priorityArray = ArrayAdapter.createFromResource(
                this, R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySelectionSpinner.setAdapter(priorityArray);        
        
        eventSelectionSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        actionSelectionSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        prioritySelectionSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //mDbHelper.deleteRule();
        
        final Context ctxt = this;
        
        final Button okButton = (Button) findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
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
            	
            	String selectedEvent = new String();
            	Spinner eventSelectionSpinner = (Spinner) findViewById(R.id.spinner);
            	selectedEvent=eventSelectionSpinner.getSelectedItem().toString();
            	System.out.println("selected event ->"+selectedEvent);

            	String selectedAction = new String();
            	Spinner actionSelectionSpinner = (Spinner) findViewById(R.id.spinner1);
            	selectedAction=actionSelectionSpinner.getSelectedItem().toString();
            	System.out.println("selected action ->"+selectedAction);
            	
            	String selectedPriority = new String();
            	Spinner prioritySelectionSpinner = (Spinner) findViewById(R.id.spinner2);
            	selectedPriority=prioritySelectionSpinner.getSelectedItem().toString();
            	System.out.println("selected action ->"+selectedPriority);
            	
            	mDbHelper.createRule(selectedgroup,selectedEvent,selectedAction,selectedPriority);
            	
               Intent displayRulesIntent=new Intent(ctxt, com.buzzters.sosync.activity.display_rules.class);
               displayRulesIntent.putExtra(Constants.SELECTED_GROUP, selectedgroup);
               displayRulesIntent.putExtra(Constants.SELECTED_EVENT, selectedEvent);
               displayRulesIntent.putExtra(Constants.SELECTED_ACTION, selectedAction);
               displayRulesIntent.putExtra(Constants.SELECTED_PRIORITY, selectedPriority);
               startActivity(displayRulesIntent); 
            }
        });
                
    }
}


