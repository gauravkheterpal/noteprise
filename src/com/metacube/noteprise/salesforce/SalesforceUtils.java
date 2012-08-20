package com.metacube.noteprise.salesforce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.metacube.noteprise.util.NotepriseLogger;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest.RestMethod;
import com.salesforce.androidsdk.rest.RestResponse;

public class SalesforceUtils 
{
	
	public static Boolean checkObjectItem(JSONObject object)
	{
		if (		object.optString("triggerable").equalsIgnoreCase("true")
				&& 	object.optString("searchable").equalsIgnoreCase("true")
				&& 	object.optString("queryable").equalsIgnoreCase("true")
			)
		{
			return true;
		}		
		return false;
	}
	
	public static Boolean filterObjectFieldForStringType(JSONObject field)
	{
		if (field.optString("type").equalsIgnoreCase("string") || field.optString("type").equalsIgnoreCase("textarea"))
		{
			if (field.optString("updateable").equalsIgnoreCase("true"))
			{
				return true;
			}			
		}		
		return false;
	}
	
	public static RestResponse publishNoteToMyChatterFeed(RestClient salesforceRestClient, String noteContent, String SF_API_VERSION)
	{
		RestResponse publishResponse = null;
		if (salesforceRestClient != null)
		{
			try 
			{
				String encodedText = URLEncoder.encode(noteContent, "UTF-8");		
				publishResponse = salesforceRestClient.sendSync(RestMethod.POST, "/services/data/" + SF_API_VERSION + "/chatter/feeds/news/me/feed-items?text=" + encodedText, null);
			} 
			catch (UnsupportedEncodingException e) 
			{
				NotepriseLogger.logError("UnsupportedEncodingException while publishing chatter feed.", NotepriseLogger.ERROR, e);
			} 
			catch (IOException e) 
			{
				NotepriseLogger.logError("IOException while publishing chatter feed.", NotepriseLogger.ERROR, e);
			}		
		}
		return publishResponse;
	}
}
