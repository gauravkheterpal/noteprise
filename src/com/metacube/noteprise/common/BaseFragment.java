package com.metacube.noteprise.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.evernote.client.oauth.android.EvernoteSession;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.base.NotepriseFragment;
import com.metacube.noteprise.util.AsyncTaskDataLoader;
import com.metacube.noteprise.util.NotepriseLogger;
import com.metacube.noteprise.util.NoteprisePreferences;
import com.metacube.noteprise.util.Utilities;
import com.salesforce.androidsdk.rest.RestClient;

public class BaseFragment extends Fragment 
{
	public EvernoteSession evernoteSession;
	public BaseActivity baseActivity;
	public LayoutInflater inflater; 
	public ViewGroup container;
	public NoteprisePreferences noteprisePreferences;
	public CommonMessageDialog commonMessageDialog;
	public RestClient salesforceRestClient;
	public String SF_API_VERSION;
	public String selectedObject, selectedField, selectedObjectLabel, selectedFieldLabel;
	public AsyncTaskDataLoader backgroundDataLoader;
	public RelativeLayout baseHeaderLayout;
	public String screenTitle;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		this.baseActivity = (BaseActivity) activity;
		this.noteprisePreferences = baseActivity.noteprisePreferences;
		this.commonMessageDialog = baseActivity.commonMessageDialog;
		this.evernoteSession = baseActivity.evernoteSession;
		this.salesforceRestClient = baseActivity.salesforceRestClient;
		this.SF_API_VERSION = baseActivity.getString(R.string.api_version);
		this.baseHeaderLayout = baseActivity.baseHeaderLayout;
	}
	
	public void initFragment(String screenTitle)
	{
		this.screenTitle = screenTitle;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.container = container;
		this.inflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public void clearContainer(ViewGroup container)
	{
		if(container != null)
		{
			container.removeAllViews();
		}			
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		if (screenTitle != null)
		{
			setHeaderTitle(screenTitle);
		}		
		updateData();
	}
	
	@Override
	public void onDetach() 
	{
		super.onDetach();
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		setHeaderTitle(getString(R.string.app_name));
	}
	
	public void setHeaderTitle(String title)
	{
		if (baseActivity.baseHeaderTitleTextView != null && Utilities.verifyStringData(title))
		{
			baseActivity.baseHeaderTitleTextView.setText(title);
		}
	}
	
	public void startEvernoteAuthentication()
	{
		baseActivity.startEvernoteAuthentication();
	}
	
	public void setupEvernoteSession()
	{
		baseActivity.setupEvernoteSession();
		this.evernoteSession = baseActivity.evernoteSession;
	}
	
	public Boolean isEvernoteAuthenticationComplete()
	{
		return baseActivity.isEvernoteAuthenticationComplete();
	}
	
	public void changeScreen(NotepriseFragment npFragment)
	{
		baseActivity.changeScreen(npFragment);
	}
	
	public void showToastNotification(String message)
	{
		baseActivity.showToastNotification(message);
	}
	
	public void finishScreen()
	{
		hideFullScreenProgresIndicator();
		hideProgresIndicator();
		baseActivity.finishScreen();
	}
	
	public void clearScreen()
	{
		hideFullScreenProgresIndicator();
		hideProgresIndicator();
		baseActivity.clearScreen();
	}
	
	public void updateData()
	{
		this.selectedField = baseActivity.SELECTED_FIELD;
		this.selectedFieldLabel = baseActivity.SELECTED_FIELD_LABEL;
		this.selectedObject = baseActivity.SELECTED_OBJECT;
		this.selectedObjectLabel = baseActivity.SELECTED_OBJECT_LABEL;
		if (Utilities.verifyStringData(selectedFieldLabel) && Utilities.verifyStringData(selectedObjectLabel))
		{
			baseActivity.selectedObjectTextView.setText(selectedObjectLabel);
			baseActivity.selectedFieldTextView.setText(selectedFieldLabel);
			baseActivity.mappingValuesContainer.setVisibility(View.VISIBLE);
		}
	}
	
	public void showFullScreenProgresIndicator()
	{
		baseActivity.showFullScreenProgresIndicator();
	}
	
	public void hideFullScreenProgresIndicator()
	{
		baseActivity.hideFullScreenProgresIndicator();
	}
	
	public void showProgresIndicator()
	{
		baseActivity.showProgresIndicator();
	}
	
	public void hideProgresIndicator()
	{
		baseActivity.hideProgresIndicator();
	}
	
	public View addViewToBaseHeaderLayout(LayoutInflater inflater, int viewLayout, int viewId)
	{
		this.inflater = inflater;
		View view = inflater.inflate(viewLayout, baseHeaderLayout);
		view = view.findViewById(viewId);
		view.setVisibility(View.GONE);
		return view;
	}
	
	public void removeViewFromBaseHeaderLayout(View view)
	{
		baseHeaderLayout.removeView(view);
	}
	
	public void executeAsyncTask()
	{
		backgroundDataLoader = new AsyncTaskDataLoader(this);
		backgroundDataLoader.execute();	
	}
	
	public void doTaskInBackground()
	{
		//Override this method in subclassses to do background tasks.
		//NotepriseLogger.logMessage("Starting AsyncTask now..");
	}
	
	public void onTaskCancelled()
	{
		
	}
	
	public void onTaskStarted()
	{
		//showProgresIndicator();
	}
	
	public void onTaskFinished()
	{
		//hideProgresIndicator();
	}
	
	public void onTaskUpdate()
	{
		
	}
}
