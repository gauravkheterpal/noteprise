package com.metacube.noteprise.salesforce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.metacube.noteprise.common.CommonListItems;
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
	
	public static RestResponse getUserFollowingData(RestClient salesforceRestClient, String SF_API_VERSION)
	{
		RestResponse publishResponse = null;
		if (salesforceRestClient != null)
		{
			try 
			{	
				publishResponse = salesforceRestClient.sendSync(RestMethod.GET, "/services/data/" + SF_API_VERSION + "/chatter/users/me/following", null);
			} 
			catch (UnsupportedEncodingException e) 
			{
				NotepriseLogger.logError("UnsupportedEncodingException while getting following data.", NotepriseLogger.ERROR, e);
			} 
			catch (IOException e) 
			{
				NotepriseLogger.logError("IOException while getting following data.", NotepriseLogger.ERROR, e);
			}		
		}
		return publishResponse;
	}
	
	public static ArrayList<CommonListItems> getListItemsFromUserFollowingResponse(RestResponse restResponse)
	{
		ArrayList<CommonListItems> responseList = new ArrayList<CommonListItems>();
		try 
		{
			String response = restResponse.asString();
			NotepriseLogger.logMessage(response);
			JSONArray responseArray = new JSONObject(response).getJSONArray("following");
			for (int i = 0; i < responseArray.length(); i++)
			{
				JSONObject subject = responseArray.getJSONObject(i).getJSONObject("subject");
				CommonListItems item = new CommonListItems();				
				item.setId(subject.optString("id"));
				item.setLabel(subject.optString("name"));
				item.setLeftImageURL(subject.getJSONObject("photo").optString("smallPhotoUrl"));
				responseList.add(item);
			}
		} 
		catch (ParseException e) 
		{				
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}				
		return responseList;
	}
	
	public static RestResponse publishNoteWithUserMentions(RestClient salesforceRestClient, String noteContent, String SF_API_VERSION, ArrayList<String> selectedIds)
	{
		RestResponse publishResponse = null;
		if (salesforceRestClient != null)
		{
			try 
			{				
				StringEntity stringEntity = new StringEntity(generateJSONBodyForChatterFeed(noteContent, selectedIds));
				stringEntity.setContentType("application/json");
				//stringEntity.setContentEncoding("UTF-8");
				String url = "/services/data/" + SF_API_VERSION + "/chatter/feeds/news/me/feed-items";
				NotepriseLogger.logMessage(url);
				publishResponse = salesforceRestClient.sendSync(RestMethod.POST, url, stringEntity);
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
	
	public static String generateJSONBodyForChatterFeed(String content, ArrayList<String> mentionIds)
	{
		JSONArray msg = new JSONArray();
		String bodyString = null;
		try 
		{			
			if (mentionIds != null)
			{
				for (int i = 0; i < mentionIds.size(); i++)
				{
					JSONObject mention = new JSONObject();				
					mention.put("type", "mention");
					mention.put("id", mentionIds.get(i));
					msg.put(mention);
				}				
			}
			if (content != null)
			{
				JSONObject text = new JSONObject();
				text.put("type", "text");
				text.put("text", " " + content);
				msg.put(text);
			}			
			JSONObject body = new JSONObject();
			body.putOpt("body", new JSONObject().put("messageSegments", msg));
			bodyString = body.toString();				
			NotepriseLogger.logMessage(bodyString);			
		}  
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return bodyString;
	}
}