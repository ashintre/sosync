package com.buzzters.sosync.activity;


import com.buzzters.sosync.activity.R;
import com.buzzters.sosync.dao.RulesDbAdapter;

//import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
//import android.widget.TextView;

public class display_rules extends ListActivity {
    /** Called when the activity is first created. */
	//private RelativeLayout rel_view;
	//private TextView txtview;
	private RulesDbAdapter mDbHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_rule);
        mDbHelper = new RulesDbAdapter(this);
        mDbHelper.open();
        
        //rel_view = (RelativeLayout) findViewById(R.id.rel_layout);
        
        Intent i = this.getIntent(); 
        String group= i.getStringExtra("group");
        String event= i.getStringExtra("event");
        String action= i.getStringExtra("action");
        
        System.out.println("group ->"+group+" event ->"+event+" action ->"+action);
        
               
        Cursor c = mDbHelper.fetchAllRules();
        startManagingCursor(c);
        String[] from = new String[] {RulesDbAdapter.KEY_GROUP,RulesDbAdapter.KEY_EVENT,RulesDbAdapter.KEY_ACTION };
                
        int[] to = new int[] { R.id.group, R.id.event,R.id.action};
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter rules =
            new SimpleCursorAdapter(this, R.layout.display_rule, c, from, to);
        setListAdapter(rules);
        
        
        final Context ctxt = this;
        
        final Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {               
               Intent launchSettingsMenuIntent = new Intent(ctxt, com.buzzters.sosync.activity.edit_settingsmenu.class);                            
               startActivity(launchSettingsMenuIntent); 
            }
        });
        
    }
}