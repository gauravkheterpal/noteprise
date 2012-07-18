package com.metacube.noteprise.salesforce;

import org.json.JSONObject;

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
}
