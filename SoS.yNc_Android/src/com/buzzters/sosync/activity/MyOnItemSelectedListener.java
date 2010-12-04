package com.buzzters.sosync.activity;

import com.buzzters.sosync.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class MyOnItemSelectedListener implements OnItemSelectedListener {
	@Override
    public void onItemSelected(AdapterView<?> parent,
        View view, int pos, long id) {
		//String ans;
		Toast.makeText(parent.getContext(), "The selected value is " +
          parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
        //ans=parent.getItemAtPosition(pos).toString();
        //return ans;
    }
	@Override
    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
}