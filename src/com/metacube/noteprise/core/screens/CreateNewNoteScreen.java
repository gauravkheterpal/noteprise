package com.metacube.noteprise.core.screens;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.common.CommonListItems;
import com.metacube.noteprise.common.CommonSpinnerAdapter;
import com.metacube.noteprise.common.Constants;
import com.metacube.noteprise.util.Utilities;

public class CreateNewNoteScreen extends BaseFragment implements OnClickListener, OnItemSelectedListener
{
	List<Notebook> notebookList;
	Spinner notebookListSpinner;
	String authToken;
	Client client;
	ArrayList<CommonListItems> spinnerItems;
	CommonSpinnerAdapter notebookSpinnerAdapter;
	EditText noteTitleEditText, noteContenteditText;
	LinearLayout createNoteButton;
	int GET_DATA = 0, SAVE_DATA = 1, CURRENT_TASK = 0;
	Note createdNote, savedNote;
	
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
    	View contentView = inflater.inflate(R.layout.create_new_note_layout, container);    	
    	notebookListSpinner = (Spinner) contentView.findViewById(R.id.create_note_notebook_list_spinner); 
    	noteTitleEditText = (EditText) contentView.findViewById(R.id.note_title_edit_text);
    	noteContenteditText = (EditText) contentView.findViewById(R.id.note_content_edit_text);
    	//createNoteButton = (Button) contentView.findViewById(R.id.create_new_note_button);
    	createNoteButton = (LinearLayout) addViewToBaseHeaderLayout(inflater, R.layout.common_save_button_layout, R.id.common_save_button);
    	createNoteButton.setOnClickListener(this);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onClick(View view) 
	{
		if (view == createNoteButton)
		{
			String noteTitle = noteTitleEditText.getText().toString().trim();
			String noteContent = noteContenteditText.getText().toString().trim();
			if (Utilities.verifyStringData(noteTitle) && Utilities.verifyStringData(noteContent))
			{
				CURRENT_TASK = SAVE_DATA;
				showFullScreenProgresIndicator();
				executeAsyncTask();
			}
			else
			{
				showToastNotification("All fields are required");
			}
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		baseActivity.createNewNoteButton.setVisibility(View.GONE);
		showProgresIndicator();
		executeAsyncTask();		
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		removeViewFromBaseHeaderLayout(createNoteButton);
		baseActivity.createNewNoteButton.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void doTaskInBackground() 
	{
		super.doTaskInBackground();
		if (CURRENT_TASK == GET_DATA)
		{
			try 
			{
				authToken = evernoteSession.getAuthToken();
	        	client = evernoteSession.createNoteStore();
	        	notebookList = client.listNotebooks(authToken);
	        	spinnerItems = new ArrayList<CommonListItems>();
	        	for (int i = 0; i < notebookList.size(); i++)
	        	{
	        		Notebook notebook = notebookList.get(i);
	        		CommonListItems item = new CommonListItems();
	        		item.setLabel(notebook.getName());
	        		item.setId(notebook.getGuid());
	        		spinnerItems.add(item);
	        	}        	
			} 
			catch (TTransportException e) 
			{
				e.printStackTrace();
			} 
			catch (EDAMUserException e) 
			{
				e.printStackTrace();
			} 
			catch (EDAMSystemException e) 
			{
				e.printStackTrace();
			} 
			catch (TException e) 
			{
				e.printStackTrace();
			}
		}
		else if (CURRENT_TASK == SAVE_DATA)
		{
			try 
			{
				authToken = evernoteSession.getAuthToken();
	        	client = evernoteSession.createNoteStore();
	        	createdNote = new Note();
	        	createdNote.setNotebookGuid(notebookSpinnerAdapter.getSpinnerItemId(notebookListSpinner.getSelectedItemPosition()));
	        	createdNote.setTitle(noteTitleEditText.getText().toString().trim());
	        	createdNote.setContent(Constants.NOTE_PREFIX + noteContenteditText.getText().toString().trim() + Constants.NOTE_SUFFIX);
	        	savedNote = client.createNote(authToken, createdNote);
			} 
			catch (TTransportException e) 
			{
				e.printStackTrace();
			} 
			catch (TException e) 
			{
				e.printStackTrace();
			} 
			catch (EDAMUserException e) 
			{
				e.printStackTrace();
			} 
			catch (EDAMSystemException e) 
			{
				e.printStackTrace();
			} 
			catch (EDAMNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		if (CURRENT_TASK == GET_DATA)
		{
			if (notebookList != null)
	        {
				hideProgresIndicator();
				createNoteButton.setVisibility(View.VISIBLE);
				notebookSpinnerAdapter = new CommonSpinnerAdapter(inflater, spinnerItems);
				notebookListSpinner.setAdapter(notebookSpinnerAdapter);
				notebookListSpinner.setOnItemSelectedListener(this);
	        }
		}
		else if (CURRENT_TASK == SAVE_DATA)
		{
			CURRENT_TASK = GET_DATA;
			hideFullScreenProgresIndicator();
			if (savedNote != null && savedNote.getGuid() != null)
			{
				showToastNotification("Note successfully created");
				baseActivity.createNewNoteButton.setVisibility(View.VISIBLE);
				finishScreen();
			}
			else
			{
				showToastNotification("Note creation failed");
			}			
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}	
}
