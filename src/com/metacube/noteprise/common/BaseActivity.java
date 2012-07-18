package com.metacube.noteprise.common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.conn.ApplicationInfo;
import com.evernote.client.oauth.android.AuthenticationResult;
import com.evernote.client.oauth.android.EvernoteSession;
import com.metacube.noteprise.common.base.NotepriseFragment;
import com.metacube.noteprise.common.base.NotepriseFragmentManager;
import com.metacube.noteprise.salesforce.SalesforceLoginUtility;
import com.metacube.noteprise.util.AsyncTaskDataLoader;
import com.metacube.noteprise.util.NoteprisePreferences;
import com.metacube.noteprise.util.Utilities;
import com.salesforce.androidsdk.rest.RestClient;

@SuppressLint("Registered")
public class BaseActivity extends FragmentActivity
{
	public ApplicationInfo evernoteAppInfo;
	public EvernoteSession evernoteSession;
	public SalesforceLoginUtility salesforceLoginUtility;
	public RestClient salesforceRestClient;
	
	public NotepriseFragmentManager notepriseFragmentManager;
	public AsyncTaskDataLoader backgroundDataLoader;
	
	public NoteprisePreferences noteprisePreferences;
	public CommonMessageDialog commonMessageDialog;
	public ProgressBar headerProgressBar;
	public ProgressDialog commonProgressDialog;
	public LinearLayout createNewNoteButton, deleteNoteButton, salesforceObjectsButton, mappingValuesContainer;
	public TextView selectedObjectTextView, selectedFieldTextView;
	public RelativeLayout baseHeaderLayout;
	
	public Boolean loggedInSalesforce = Boolean.FALSE;
	public Boolean loggedInEvernote = Boolean.FALSE;
	
	public String SELECTED_OBJECT, SELECTED_OBJECT_LABEL;
	public String SELECTED_FIELD, SELECTED_FIELD_LABEL;
	
	//Main menu save data..
	public CommonListAdapter savedListAdapter = null;
	public Integer savedCurrentTask = null;
	public String savedQueryString = null;
	public Integer savedSelectedRadioButtonId = null;
	public Boolean isDataSaved = Boolean.FALSE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		noteprisePreferences = new NoteprisePreferences(this);
		commonMessageDialog = new CommonMessageDialog(this);
		backgroundDataLoader = new AsyncTaskDataLoader(this);
		commonProgressDialog = new ProgressDialog(this);
		commonProgressDialog.setTitle(Messages.PROGRESS_DIALOG_TITLE);
		commonProgressDialog.setMessage(Messages.PROGRESS_DIALOG_MESSAGE);
	}	
	
	public void startEvernoteAuthentication() 
	{
		setupEvernoteSession();
		if (!evernoteSession.isLoggedIn()) 
		{
			evernoteSession.authenticate(this);			
		}
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		if (salesforceLoginUtility != null && !loggedInSalesforce)
		{
			salesforceLoginUtility.onAppPause();
		}
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		if (salesforceLoginUtility != null && !loggedInSalesforce)
		{
			salesforceLoginUtility.onAppResume();
		}		
	}
	
	@Override
	public void onUserInteraction() 
	{
		super.onUserInteraction();
		if (salesforceLoginUtility != null && !loggedInSalesforce)
		{
			salesforceLoginUtility.onAppUserInteraction();
		}
	}

	public void setupEvernoteSession() 
	{		
		evernoteAppInfo = new ApplicationInfo(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, Constants.EVERNOTE_HOST, 
				Constants.APP_NAME, Constants.APP_VERSION);
	    if (noteprisePreferences.isSignedInToEvernote()) 
	    {
	    	AuthenticationResult authResult = new AuthenticationResult(noteprisePreferences.getEvetnoteAuthToken(), noteprisePreferences.getEvetnoteNoteStoreUrl(), 
	    			noteprisePreferences.getEvernoteWebApiPrefix(), noteprisePreferences.getEvetnoteUserId());
	    	evernoteSession = new EvernoteSession(evernoteAppInfo, authResult, Utilities.getTempStorageDirectory());	    	
	    } 
	    else 
	    {
	    	evernoteSession = new EvernoteSession(evernoteAppInfo, Utilities.getTempStorageDirectory());
	    }
	}
	
	public Boolean isEvernoteAuthenticationComplete()
	{
		if  (evernoteSession != null)
		{
			if (evernoteSession.completeAuthentication() || evernoteSession.isLoggedIn())
			{
				return true;
			}
		}
		return false;
	}
	
	public void changeScreen(NotepriseFragment npFragment)
	{
		notepriseFragmentManager.changeScreen(npFragment);
	}
	
	public void clearScreen()
	{
		notepriseFragmentManager.clearScreen();
	}
	
	public void showFullScreenProgresIndicator()
	{
		if (commonProgressDialog != null && !commonProgressDialog.isShowing())
		{
			commonProgressDialog.show();			
		}
	}
	
	public void hideFullScreenProgresIndicator()
	{
		if (commonProgressDialog != null && commonProgressDialog.isShowing())
		{
			commonProgressDialog.dismiss();
		}
	}
	
	public void showProgresIndicator()
	{
		if (headerProgressBar != null)
		{
			headerProgressBar.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideProgresIndicator()
	{
		if (headerProgressBar != null)
		{
			headerProgressBar.setVisibility(View.GONE);
		}
	}
	
	public void showToastNotification(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public void handleSalesforceLoginComplete()
	{
		
	}
	
	public void finishScreen()
	{
		notepriseFragmentManager.onBackPressed();
	}
	
	public void executeAsyncTask()
	{
		backgroundDataLoader = new AsyncTaskDataLoader(this);
		backgroundDataLoader.execute();		
	}
	
	public void doTaskInBackground()
	{
		//Override this method in subclassses to do background tasks.
	}
	
	public void onTaskCancelled()
	{
		
	}
	
	public void onTaskStarted()
	{
		
	}
	
	public void onTaskFinished()
	{
		
	}
	
	public void onTaskUpdate()
	{
		
	}
}