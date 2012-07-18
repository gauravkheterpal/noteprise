package com.metacube.noteprise.core.screens;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.transport.TTransportException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Notebook;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.common.CommonListAdapter;
import com.metacube.noteprise.common.CommonListItems;
import com.metacube.noteprise.common.base.NotepriseFragment;
import com.metacube.noteprise.evernote.EvernoteUtils;
import com.metacube.noteprise.util.Utilities;

public class MainMenuScreen extends BaseFragment implements OnClickListener, OnItemClickListener, OnCheckedChangeListener
{
	List<Notebook> notebookList;
	ListView listView;
	String authToken;
	Client client;
	NoteList noteList;
	ArrayList<CommonListItems> listItems;
	CommonListAdapter noteListAdapter;
	RadioGroup searchCriteriaRadioGroup;
	ImageButton searchButton;
	EditText searchQueryEditText;
	String queryString;
	Integer selectedRadioButtonId;
	Boolean isDataRestored = Boolean.FALSE;
	Integer GET_ALL_NOTEBOOKS = 0, SEARCH_NOTEBOOK = 1, SEARCH_TAG = 2, SEARCH_KEYWORD = 3, TASK = 0;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	clearContainer(container);
    	View contentView = inflater.inflate(R.layout.home_screen_layout, container);
        listView = (ListView) contentView.findViewById(R.id.notes_list_view);  
        searchCriteriaRadioGroup = (RadioGroup) contentView.findViewById(R.id.search_criteria_radio_group);
        searchCriteriaRadioGroup.setOnCheckedChangeListener(this);
        searchQueryEditText = (EditText) contentView.findViewById(R.id.search_query_edit_text);
        searchButton = (ImageButton) contentView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onClick(View view) 
	{
		if (view == searchButton)
		{
			String queryString = searchQueryEditText.getText().toString().trim();
			if (!Utilities.verifyStringData(queryString))
			{
				showToastNotification("Please enter text first to search");
				return;
			}
			showFullScreenProgresIndicator();
			listView.setAdapter(null);
			switch(searchCriteriaRadioGroup.getCheckedRadioButtonId())
			{
				case R.id.search_notebook_radio_button:
				{
					TASK = SEARCH_NOTEBOOK;
					executeAsyncTask();
					break;
				}
				case R.id.search_tag_radio_button:
				{
					TASK = SEARCH_TAG;
					executeAsyncTask();
					break;
				}
				case R.id.search_keyword_radio_button:
				{
					TASK = SEARCH_KEYWORD;
					executeAsyncTask();
					break;
				}
				default:
				{
					TASK = GET_ALL_NOTEBOOKS;
				}
			}
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
		
		
		if (isEvernoteAuthenticationComplete())
		{
			if (baseActivity.isDataSaved)
			{
				loadPreviousState();
			}
			else
		    {
				getAllNotes();
		    }			
	    }
		else
		{
			
		}
	}
	
	@Override
	public void doTaskInBackground() 
	{
		super.doTaskInBackground();		
		try 
		{
			authToken = evernoteSession.getAuthToken();
			client = evernoteSession.createNoteStore();
			queryString = searchQueryEditText.getText().toString().trim();
			if (TASK == GET_ALL_NOTEBOOKS)
			{
				listItems = EvernoteUtils.getAllNotes(authToken, client);
			}
			else if (TASK == SEARCH_NOTEBOOK)
			{
				listItems = EvernoteUtils.searchNotebooks(authToken, client, queryString);
			}
			else if (TASK == SEARCH_TAG)
			{
				listItems = EvernoteUtils.searchTags(authToken, client, queryString);
			}
			else if (TASK == SEARCH_KEYWORD)
			{
				listItems = EvernoteUtils.searchKeywords(authToken, client, queryString);
			}
		} 
		catch (TTransportException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		hideFullScreenProgresIndicator();
		if (listItems != null)
        {
			noteListAdapter = new CommonListAdapter(inflater, listItems);
			listView.setAdapter(noteListAdapter);
        	listView.setOnItemClickListener(this);
        	TASK = GET_ALL_NOTEBOOKS;
        }
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) 
	{
		if (noteListAdapter.isListItem(position))
		{
			String noteGuid = noteListAdapter.getListItemId(position);
			Bundle args = new Bundle();
			args.putString("noteGuid", noteGuid);
			changeScreen(new NotepriseFragment("NoteDetails", NoteDetailsScreen.class, args));
		}		
	}
	
	public void getAllNotes()
	{
		if (evernoteSession != null)
	    {
			setSearchBarEnabled(Boolean.FALSE);
			showFullScreenProgresIndicator();
			executeAsyncTask();
	    }
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		saveCurrentState();
	}
	
	public void saveCurrentState()
	{
		baseActivity.savedListAdapter = noteListAdapter;
		baseActivity.savedCurrentTask = TASK;
		baseActivity.savedQueryString = queryString;
		baseActivity.savedSelectedRadioButtonId = searchCriteriaRadioGroup.getCheckedRadioButtonId();
		baseActivity.isDataSaved = Boolean.TRUE;
	}
	
	public void loadPreviousState()
	{
		if (baseActivity.savedListAdapter != null)
		{
			noteListAdapter = baseActivity.savedListAdapter;
			listView.setAdapter(noteListAdapter);
			listView.setOnItemClickListener(this);
		}
		if (baseActivity.savedQueryString != null)
		{
			queryString = baseActivity.savedQueryString;
			searchQueryEditText.setText(queryString);
		}
		if (baseActivity.savedCurrentTask != null)
		{
			TASK = baseActivity.savedCurrentTask;
		}
		if (baseActivity.savedSelectedRadioButtonId != null)
		{
			isDataRestored = Boolean.TRUE;
			selectedRadioButtonId = baseActivity.savedSelectedRadioButtonId;
			searchCriteriaRadioGroup.check(selectedRadioButtonId);
			if (selectedRadioButtonId == R.id.search_all_radio_button)
			{
				setSearchBarEnabled(Boolean.FALSE);
			}
		}
		
		if (noteListAdapter == null)
		{
			getAllNotes();
		}
	}
	
	public void setSearchBarEnabled(Boolean status)
	{
		searchQueryEditText.setEnabled(status);
		searchButton.setEnabled(status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) 
	{
		if (group == searchCriteriaRadioGroup && checkedId == R.id.search_all_radio_button)
		{
			TASK = GET_ALL_NOTEBOOKS;
			if (isDataRestored)
			{
				isDataRestored = Boolean.FALSE;
			}
			else
			{
				getAllNotes();
			}					
		}
		else if (group == searchCriteriaRadioGroup && checkedId != R.id.search_all_radio_button)
		{
			setSearchBarEnabled(Boolean.TRUE);
		}
	}	
}
