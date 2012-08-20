package com.metacube.noteprise.salesforce;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommonSOQL 
{
	public static final String SELECT_QUERY_PREFIX = "SELECT id, ";
	public static final String DISPLAY_LABEL_FIELD = "name";
	public static final String FROM = " from ";
	public static final String ORDER_BY_SUFFIX = " ORDER BY ";
			
	public static String getQueryForObject(String object)
	{
		HashMap<String, String> OBJECT_FIELD_MAP = new LinkedHashMap<String, String>();	   
		OBJECT_FIELD_MAP.put("Case", "CaseNumber");
		OBJECT_FIELD_MAP.put("CaseComment", "ParentId");
		OBJECT_FIELD_MAP.put("ContentVersion", "ContentDocumentId");
		OBJECT_FIELD_MAP.put("Contract", "ContractNumber");
		OBJECT_FIELD_MAP.put("Event", "Subject");
		OBJECT_FIELD_MAP.put("FeedComment", "FeedItemId");
		OBJECT_FIELD_MAP.put("Idea", "Title");
		OBJECT_FIELD_MAP.put("Note", "Title");
		OBJECT_FIELD_MAP.put("Solution", "SolutionName");
		OBJECT_FIELD_MAP.put("Task", "Subject");		
		
		String displayField = DISPLAY_LABEL_FIELD;
		if (OBJECT_FIELD_MAP.get(object) != null)
		{
			displayField = OBJECT_FIELD_MAP.get(object);
		}		
		String query = SELECT_QUERY_PREFIX + displayField + FROM + object + ORDER_BY_SUFFIX + displayField;
		return query;
	}
	
	public static String getUpdateChatterFeedQuery()
	{
		return null;
	}
}
