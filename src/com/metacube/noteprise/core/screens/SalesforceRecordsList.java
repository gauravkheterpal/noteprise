package com.metacube.noteprise.core.screens;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.common.CommonListAdapter;
import com.metacube.noteprise.common.CommonListItems;
import com.metacube.noteprise.common.Messages;
import com.metacube.noteprise.salesforce.CommonSOQL;
import com.metacube.noteprise.util.NotepriseLogger;
import com.metacube.noteprise.util.Utilities;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

public class SalesforceRecordsList extends BaseFragment implements OnItemClickListener, AsyncRequestCallback, OnClickListener
{
	ListView listView;
	String noteContent;
	RestRequest recordsRequest, updateRecordRequest;
	CommonListAdapter recordsAdapter;
	TextView noResultsTextView;
	LinearLayout editRecordSelectionButton, saveRecordSelectionButton;
	int totalRequests = 0;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        noteContent = Utilities.getStringFromBundle(getArguments(), "noteContent");        
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
    	noResultsTextView = (TextView) contentView.findViewById(R.id.common_list_no_results);
    	noResultsTextView.setVisibility(View.GONE);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
	public void onResume() 
	{
		super.onResume();
		if (salesforceRestClient != null)
		{
			List<String> fieldList = new ArrayList<String>();
			fieldList.add("id");
			fieldList.add("name");
			try 
			{
				showFullScreenProgresIndicator();
				recordsRequest = RestRequest.getRequestForQuery(SF_API_VERSION, CommonSOQL.getQueryForObject(selectedObject));				
			} 
			catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
			salesforceRestClient.sendAsync(recordsRequest, this);			
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
	{
		if (recordsAdapter.isCheckListMode())
		{
			recordsAdapter.setChecedkCurrentItem(position);
		}
		else
		{
			showFullScreenProgresIndicator();
			String recordId = recordsAdapter.getListItemId(position);
			sendUpdateRequest(recordId);
		}		
	}
	
	public void sendUpdateRequest(String recordId)
	{
		Map<String, Object> fields = new LinkedHashMap<String, Object>();
		fields.put(selectedField, noteContent);
		try 
		{
			updateRecordRequest = RestRequest.getRequestForUpdate(SF_API_VERSION, selectedObject, recordId, fields);
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		salesforceRestClient.sendAsync(updateRecordRequest, this);
	}

	@Override
	public void onSuccess(RestRequest request, RestResponse response) 
	{
		if (request == recordsRequest)
		{
			try 
			{
				NotepriseLogger.logMessage(response.asString());
				JSONObject responseObject = response.asJSONObject();
				JSONArray records = responseObject.getJSONArray("records");
				ArrayList<CommonListItems> items = new ArrayList<CommonListItems>();
				for (int i = 0; i < records.length(); i++)
				{
					CommonListItems item = new CommonListItems();
					JSONObject object = records.getJSONObject(i);
					item.setLabel(object.optString("Name"));
					item.setId(object.optString("Id"));
					item.setLeftImage(R.drawable.settings_gray);
					items.add(item);
				}
				hideFullScreenProgresIndicator();
				if (items != null && items.size() > 0)
				{
					editRecordSelectionButton.setVisibility(View.VISIBLE);
					recordsAdapter = new CommonListAdapter(inflater, items);
					listView.setAdapter(recordsAdapter);
					listView.setOnItemClickListener(this);
				}
				else
				{
					noResultsTextView.setVisibility(View.VISIBLE);
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
		}
		else if (request == updateRecordRequest)
		{
			try 
			{
				NotepriseLogger.logMessage(response.asString());
				if (response.getStatusCode() == 204 && !response.asString().contains("errorCode"))
				{
					//commonMessageDialog.showMessageDialog("Record successfully updated");
					if (totalRequests > 0)
					{
						hideFullScreenProgresIndicator();
						showToastNotification(Messages.RECORD_UPDATED_MESSAGE);
						clearScreen();
					}
					else
					{
						showToastNotification(Messages.RECORD_UPDATED_MESSAGE);
						hideFullScreenProgresIndicator();
						clearScreen();
					}
					
				}
				else if (response.asString().contains("errorCode"))
				{
					hideFullScreenProgresIndicator();
					showToastNotification("Failed saving records.");
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
		NotepriseLogger.logError("Exception getting response for records list.", NotepriseLogger.ERROR, exception);	
		commonMessageDialog.showMessageDialog("Some error occurred.");
	}

	@Override
	public void onClick(View view) 
	{
		if (view == editRecordSelectionButton)
		{
			editRecordSelectionButton.setVisibility(View.GONE);
			saveRecordSelectionButton.setVisibility(View.VISIBLE);
			recordsAdapter.showCheckList();
		}
		else if (view == saveRecordSelectionButton)
		{			
			ArrayList<String> selectedRecords = recordsAdapter.getCheckedItemsList();
			if (selectedRecords.size() > 0)
			{
				showFullScreenProgresIndicator();
				totalRequests = selectedRecords.size();
				for (int i = 0; i < selectedRecords.size(); i++)
				{
					sendUpdateRequest(selectedRecords.get(i));
				}
			}
			else
			{
				showToastNotification("No records selected");
			}
		}
	}
}
