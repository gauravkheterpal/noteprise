package com.metacube.noteprise.core.screens;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.common.CommonListAdapter;
import com.metacube.noteprise.common.CommonListItems;
import com.metacube.noteprise.salesforce.SalesforceUtils;
import com.metacube.noteprise.util.NotepriseLogger;
import com.metacube.noteprise.util.Utilities;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class PublishToChatterRecordsListScreen extends BaseFragment implements AsyncRequestCallback, OnClickListener, OnItemClickListener
{
	String publishString;
	RestResponse getFollowingDataResponse = null, publishResponse;
	Button publishToChatterButton;
	Integer GET_FOLLOWING_DATA = 0, PUBLISH_TO_CHATTER_USER = 1, TASK = 0;
	ArrayList<CommonListItems> listItems;
	CommonListAdapter listAdapter;
	ListView listView;
	ArrayList<String> selectedIds = null;
	LinearLayout editRecordSelectionButton, saveRecordSelectionButton;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        publishString = Utilities.getStringFromBundle(getArguments(), "publishString");        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	clearContainer(container);
    	View contentView = inflater.inflate(R.layout.common_list_layout, container);
    	listView = (ListView) contentView.findViewById(R.id.common_list_view);
    	editRecordSelectionButton = (LinearLayout) addViewToBaseHeaderLayout(inflater, R.layout.common_edit_button_layout, R.id.common_edit_button);
    	saveRecordSelectionButton = (LinearLayout) addViewToBaseHeaderLayout(inflater, R.layout.common_save_button_layout, R.id.common_save_button);
    	editRecordSelectionButton.setOnClickListener(this);
    	saveRecordSelectionButton.setOnClickListener(this);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		TASK = GET_FOLLOWING_DATA;
		showFullScreenProgresIndicator();
		executeAsyncTask();
	}
	
	@Override
	public void doTaskInBackground() 
	{
		super.doTaskInBackground();
		if (TASK == GET_FOLLOWING_DATA)
		{
			if (salesforceRestClient != null)
			{
				getFollowingDataResponse = SalesforceUtils.getUserFollowingData(salesforceRestClient, SF_API_VERSION);
			}
		}
		else if (TASK == PUBLISH_TO_CHATTER_USER)
		{
			if (salesforceRestClient != null)
			{
				publishResponse = SalesforceUtils.publishNoteWithUserMentions(salesforceRestClient, publishString, SF_API_VERSION, selectedIds);
			}
		}
		
	}
	
	public void publishNoteToChatterFeed()
	{
		TASK = PUBLISH_TO_CHATTER_USER;
		showFullScreenProgresIndicator();
		executeAsyncTask();
	}

	@Override
	public void onSuccess(RestRequest request, RestResponse response) 
	{
				
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		hideFullScreenProgresIndicator();	
		if (TASK == GET_FOLLOWING_DATA)
		{
			if (getFollowingDataResponse != null)
			{
				listItems = SalesforceUtils.getListItemsFromUserFollowingResponse(getFollowingDataResponse);
				if (listItems != null && listItems.size() > 0)
				{
					listAdapter = new CommonListAdapter(inflater, listItems);
					listView.setAdapter(listAdapter);
					listView.setOnItemClickListener(this);
					editRecordSelectionButton.setVisibility(View.VISIBLE);
				}
			}
		}
		else if (TASK == PUBLISH_TO_CHATTER_USER)
		{
			TASK = GET_FOLLOWING_DATA;
			try 
			{
				String response = publishResponse.asString();
				NotepriseLogger.logMessage(response);
				if (!response.contains("error"))
				{
					showToastNotification("Note was successfully posted on feed.");
					finishScreen();
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
		}		
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		removeViewFromBaseHeaderLayout(editRecordSelectionButton);
		removeViewFromBaseHeaderLayout(saveRecordSelectionButton);
	}

	@Override
	public void onError(Exception exception) 
	{
		hideFullScreenProgresIndicator();
		NotepriseLogger.logError("Exception publishing chatter feed.", NotepriseLogger.ERROR, exception);	
		commonMessageDialog.showMessageDialog("Some error occurred.");
	}

	@Override
	public void onClick(View view) 
	{
		if (view == publishToChatterButton)
		{
			publishNoteToChatterFeed();
		}
		else if (view == editRecordSelectionButton)
		{
			editRecordSelectionButton.setVisibility(View.GONE);
			saveRecordSelectionButton.setVisibility(View.VISIBLE);
			listAdapter.showCheckList();
		}
		else if (view == saveRecordSelectionButton)
		{
			selectedIds = listAdapter.getCheckedItemsList();
			if (selectedIds.size() > 0)
			{
				showFullScreenProgresIndicator();
				TASK = PUBLISH_TO_CHATTER_USER;
				showFullScreenProgresIndicator();
				executeAsyncTask();
			}
			else
			{
				showToastNotification("No records selected");
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
	{
		if (listAdapter.isCheckListMode())
		{
			listAdapter.setChecedkCurrentItem(position);
		}
		else if (listItems != null)
		{
			String recordId = listItems.get(position).getId();
			selectedIds = new ArrayList<String>();
			selectedIds.add(recordId);
			TASK = PUBLISH_TO_CHATTER_USER;
			showFullScreenProgresIndicator();
			executeAsyncTask();
		}
	}
}