package com.metacube.noteprise.common;

public class Constants 
{
	public static final String CONSUMER_KEY = "noteprise-3933";
	public static final String CONSUMER_SECRET = "ce361e9ac663ad4a";
	
	public static final Boolean DEBUGGING_ENABLED = true;
	public static final Boolean STACKTRACE_ENABLED = true;
	
	public static final String NOTEPRISE_PREFS = "NoteprisePrefs";	
	public static final String EVERNOTE_LOGGED_IN_PREF = "evernote_loggedin";
	public static final String EVERNOTE_AUTH_TOKEN = "evernote_auth_token";
	public static final String EVERNOTE_NOTESTORE_URL = "evernote_notestore_url";
	public static final String EVERNOTE_USER_ID = "evernote_user_id";
	public static final String EVERNOTE_WEBAPI_PREFIX = "evernote_webapi_prefix";
	
	
	public static final String APP_DATA_PATH = "/Android/data/com.metacube.noteprise/data/";		
	//public static final String EVERNOTE_HOST = "sandbox.evernote.com";	
	public static final String EVERNOTE_HOST = "www.evernote.com";
		  
	public static final String APP_NAME = "Noteprise";  
	public static final String APP_VERSION = "1.0";
	
	public static final String NOTE_PREFIX = 
		    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		    "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">" +
		    "<en-note><div>";
	public static final String NOTE_SUFFIX = "</div></en-note>";
	
	public static final Integer MAX_NOTES = 100;
	
	
	public static final String ITEM_TYPE_LIST_SECTION = "LIST_SECTION";
	public static final String ITEM_TYPE_LIST_ITEM = "LIST_ITEM";
	
}
