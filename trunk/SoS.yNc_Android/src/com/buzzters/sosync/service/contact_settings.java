package com.buzzters.sosync.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Service;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import com.buzzters.sosync.dao.CalendarDBAdapter;
import com.buzzters.sosync.dao.RulesDbAdapter;
import com.buzzters.sosync.utility.Constants;


public class contact_settings extends Service 
{

	private CalendarDBAdapter calendarAdapter;
	private RulesDbAdapter rulesAdapter;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onStart(Intent intent, int starId)
	{
			calendarAdapter = new CalendarDBAdapter(this);
			calendarAdapter.open();

			rulesAdapter = new RulesDbAdapter(this);
			rulesAdapter.open();

			String ringtone=intent.getStringExtra("ringtone_uri");
			Date startDate = null;
			Date endDate = null, sys = null;
			Cursor calendarEntriesCursor = calendarAdapter.fetchAllRules();
			calendarEntriesCursor.moveToFirst();
			while(!calendarEntriesCursor.isAfterLast())
			{
				String calendar_event=calendarEntriesCursor.getString(calendarEntriesCursor.getColumnIndex(CalendarDBAdapter.KEY_EVENT));
				String start_time=calendarEntriesCursor.getString(calendarEntriesCursor.getColumnIndex(CalendarDBAdapter.KEY_STARTTIME)).replace("T", "");
				String end_time=calendarEntriesCursor.getString(calendarEntriesCursor.getColumnIndex(CalendarDBAdapter.KEY_ENDTIME)).replace("T", "");
				Log.d(Constants.APP_TAG, "Start time : " + start_time + "End time : " + end_time + "Event : " + calendar_event);

				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
				try {
					startDate = (Date)sdf.parse(start_time);
					endDate=(Date)sdf.parse(end_time);
					sys=new Date();
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if(startDate.before(sys)&&endDate.after(sys))
				{
					Cursor ruleEntriesCursor = rulesAdapter.fetchRule_event(calendar_event);
					String rule_action=ruleEntriesCursor.getString(ruleEntriesCursor.getColumnIndex(RulesDbAdapter.KEY_ACTION));
					System.out.println("rule action is ->"+rule_action);
					ruleEntriesCursor.close();
	
					if(rule_action.equals("Ring as Silent"))
					{
						switch_silentmode();
						System.out.println("Switched to silent mode");
					}
					else
					{
						ContentResolver cr = getContentResolver();
						Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
						if (cur.getCount() > 0) {
							while (cur.moveToNext()) {
								String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
								System.out.println("contact name ->"+name);
	
								ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
								if(rule_action.equalsIgnoreCase("Send to Voicemail")){
									ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
											.withSelection(Data.DISPLAY_NAME + "=?", new String[]{name})
											.withValue(RawContacts.SEND_TO_VOICEMAIL, 1)
											.build());
									Log.d(Constants.APP_TAG, "Setting " + name + " to " + "SEND_TO_VOICEMAIL");
								}
								else{
									ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
											.withSelection(Data.DISPLAY_NAME + "=?", new String[]{name})
											.withValue(RawContacts.CUSTOM_RINGTONE, ringtone)
											.build());
									Log.d(Constants.APP_TAG, "Setting " + name + " to " + "CUSTOM_RINGTONE");
								}
								try {
									getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
									//System.out.println("DONE !!");
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (OperationApplicationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				calendarEntriesCursor.moveToNext();
			}
			System.out.println("DONE !!!!");
	}

	public void switch_silentmode()
	{
		AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

}