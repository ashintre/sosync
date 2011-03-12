package com.buzzters.sosync.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

import com.buzzters.sosync.model.ContactInfo;
import com.facebook.android.Facebook;

public class FBContactsFetcherThread extends Thread
{
	Facebook fb;
	String listId;
	String listName;
	Map<String, List<ContactInfo>> listMembersMap;
	Context ctxt;
	
	private static final Bundle bundleParams = new Bundle();
	private static final String NOTES = "Contact group imported from Facebook";
	
	static
	{
		bundleParams.putString("fields", "id,name,picture");
	}
	
	public FBContactsFetcherThread(Facebook fb, Context ctxt, String listId, String listName, Map<String, List<ContactInfo>> listMembersMap)
	{
		this.fb = fb;
		this.listMembersMap = listMembersMap;
		this.listId = listId;
		this.listName = listName;
		this.ctxt = ctxt;
	}
	
	@Override
	public void run()
	{
		try 
		{
			String listMembersAsJsonString = fb.request(listId +"/members");
			listMembersMap.put(listId, getListMembers(listMembersAsJsonString));
			ContentValues cValues = new ContentValues();
			cValues.put(Groups.TITLE, listName);
			cValues.put(Groups.NOTES, NOTES);			
			Uri dataUri = ctxt.getContentResolver().insert(ContactsContract.Groups.CONTENT_URI, cValues);
			long groupId = ContentUris.parseId(dataUri);
			for(ContactInfo cInfo : listMembersMap.get(listId))
			{									 
				ContentValues values = new ContentValues();
				values.putNull(RawContacts.ACCOUNT_TYPE);
				values.putNull(RawContacts.ACCOUNT_NAME);
								
				Uri rawContactUri = ctxt.getContentResolver().insert(RawContacts.CONTENT_URI, values);
				long rawContactId = ContentUris.parseId(rawContactUri);
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
				values.put(StructuredName.DISPLAY_NAME, cInfo.getContactName());
				ctxt.getContentResolver().insert(Data.CONTENT_URI, values);
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, GroupMembership.CONTENT_ITEM_TYPE);
				values.put(GroupMembership.GROUP_ROW_ID, groupId);
				ctxt.getContentResolver().insert(Data.CONTENT_URI, values);
				
				// Get the profile picture
				setContactPhotoFromEntity(rawContactId, getContactProfilePic(cInfo), cInfo);
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}				
	}
	
	private void setContactPhotoFromEntity(long rawContactId, HttpEntity imageEntity, ContactInfo cInfo)
	{
		ContentValues values = new ContentValues();
	    int photoRow = -1;
	    String where = ContactsContract.Data.RAW_CONTACT_ID + " = " + rawContactId + " AND " + ContactsContract.Data.MIMETYPE + "=='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
	    Cursor cursor = ctxt.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
	    int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
	    if (cursor.moveToFirst()) {
	        photoRow = cursor.getInt(idIdx);
	    }
	    cursor.close();
	    
	    InputStream is = null;
	    try
	    {
			is = imageEntity.getContent();
			String state = Environment.getExternalStorageState();
			if(Environment.MEDIA_MOUNTED.equals(state)){
				Log.d(Constants.APP_TAG, "Media is mounted. Lets start storing stuff");
				File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				picDirectory.mkdirs();
				File contactPicFile = new File(picDirectory, cInfo.getContactName()+".jpg");
				Log.d(Constants.APP_TAG, "contact file path " + contactPicFile.getCanonicalPath());
				if(true && contactPicFile.createNewFile()){
					Writer oWriter = null;
					try{
						oWriter = new BufferedWriter(new FileWriter(contactPicFile));
						int ch;
						while((ch = is.read()) > 0){
							oWriter.write(ch);
						}
					}
					finally{
						oWriter.close();
					}
				}
				else{
					Log.e(Constants.APP_TAG, "Unable to create file for " + cInfo.getContactName());
				}
			}
			/*byte [] pictureBlob = is.toString().getBytes();
	    	values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
		    values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		    values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, pictureBlob);
		    values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
		 
		    if (photoRow >= 0) {
		    	ctxt.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.Data._ID + " = " + photoRow, null);
		    } else {
		    	ctxt.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
		    }*/
	    } catch (IllegalStateException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	    finally
	    {
	    	if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }	    
	}
	
	private List<ContactInfo> getListMembers(String jsonFriendList) throws JSONException, MalformedURLException, IOException
	{
		List<ContactInfo> contactsList = new ArrayList<ContactInfo>();
		JSONObject friendList = new JSONObject(jsonFriendList);
		JSONArray friendListArray = friendList.getJSONArray("data");
		for(int index = 0; index < friendListArray.length(); index++)
		{
			ContactInfo cInfo = new ContactInfo();
			cInfo.setListId(listId);
			String friendId = friendListArray.getJSONObject(index).getString("id"); 
			cInfo.setFbId(friendId);
			//String friendDetailsJSON = fb.request(friendId, bundleParams);
			cInfo.setContactName(friendListArray.getJSONObject(index).getString("name"));
			//cInfo.setPictureUrl(new JSONObject(friendDetailsJSON).getString("picture"));			
			contactsList.add(cInfo);
		}
		return contactsList;
	}
	
	private HttpEntity getContactProfilePic(ContactInfo cInfo) throws ClientProtocolException, IOException
	{
		HttpParams params = new BasicHttpParams();
		params.setParameter("access_token", fb.getAccessToken());
		params.setParameter("type", "small");
		DefaultHttpClient httpclient = new DefaultHttpClient(params);
        HttpGet httpGet = new HttpGet("https://graph.facebook.com/" + cInfo.getFbId()
        						+"/picture");
        HttpResponse httpResponse = httpclient.execute(httpGet);
        return httpResponse.getEntity();        
	}
		
}
