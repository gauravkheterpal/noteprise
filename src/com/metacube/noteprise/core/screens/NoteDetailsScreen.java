package com.metacube.noteprise.core.screens;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.common.base.NotepriseFragment;
import com.metacube.noteprise.evernote.EvernoteUtils;
import com.metacube.noteprise.util.Utilities;

public class NoteDetailsScreen extends BaseFragment implements OnClickListener, android.content.DialogInterface.OnClickListener
{
	String authToken;
	Client client;
	TextView noteTitleTextView, noteContentTextView;
	LinearLayout saveButton;
	String noteTitle, noteContent, noteGuid;
	Note note;
	Integer GET_NOTE_DATA = 0, DELETE_NOTE = 1, TASK = 0, deletionId = null;
	
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        noteGuid = Utilities.getStringFromBundle(args, "noteGuid");
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	clearContainer(container);
    	View contentView = inflater.inflate(R.layout.note_detail_screen_layout, container);
    	noteTitleTextView = (TextView) contentView.findViewById(R.id.note_title_text_view);    	
    	noteContentTextView = (TextView) contentView.findViewById(R.id.note_content_text_view);
    	saveButton = (LinearLayout) addViewToBaseHeaderLayout(inflater, R.layout.common_save_button_layout, R.id.common_save_button);
    	baseActivity.deleteNoteButton.setOnClickListener(this);
    	saveButton.setOnClickListener(this);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

	@Override
	public void onClick(View view) 
	{
		if (view == saveButton && baseActivity.SELECTED_OBJECT != null && baseActivity.SELECTED_FIELD != null)
		{
			Bundle args = new Bundle();
			args.putString("noteContent", noteContent);
			changeScreen(new NotepriseFragment("RecordsList", SalesforceRecordsList.class, args));
		}
		else if (view == baseActivity.deleteNoteButton)
		{
			try 
			{
				authToken = evernoteSession.getAuthToken();
	        	client = evernoteSession.createNoteStore();
			}
			catch (TException e) 
			{
				e.printStackTrace();
			}
			commonMessageDialog.showDeleteNoteDialog(authToken, client, this);
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		baseActivity.createNewNoteButton.setVisibility(View.GONE);
		baseActivity.deleteNoteButton.setVisibility(View.VISIBLE);
		showProgresIndicator();
		executeAsyncTask();			
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		removeViewFromBaseHeaderLayout(saveButton);
		baseActivity.createNewNoteButton.setVisibility(View.VISIBLE);
		baseActivity.deleteNoteButton.setVisibility(View.GONE);
	}
	
	@Override
	public void doTaskInBackground() 
	{
		super.doTaskInBackground();
		if (TASK == GET_NOTE_DATA)
		{
			if (evernoteSession != null)
		    {
				try 
				{
					authToken = evernoteSession.getAuthToken();
		        	client = evernoteSession.createNoteStore();
		        	note = client.getNote(authToken, noteGuid, true, false, false, false);
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
				catch (EDAMNotFoundException e) 
				{
					e.printStackTrace();
				}
		    }
		}
		else if (TASK == DELETE_NOTE)
		{
			deletionId = EvernoteUtils.deleteNote(authToken, client, noteGuid);
		}
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		if (TASK == GET_NOTE_DATA)
		{
			hideProgresIndicator();
			//noteContent = client.getNoteContent(authToken, noteGuid);
	    	noteTitle = note.getTitle();
	    	noteContent = EvernoteUtils.stripNoteContent(note.getContent());	        	
	    	noteTitleTextView.setText(noteTitle);
	    	noteContentTextView.setText(noteContent);
	    	saveButton.setVisibility(View.VISIBLE);
		}
		else if(TASK == DELETE_NOTE)
		{
			TASK = GET_NOTE_DATA;
			hideFullScreenProgresIndicator();
			if (deletionId != null)
			{
				showToastNotification("Note deleted successfully");
				finishScreen();
			}
			else
			{
				showToastNotification("Note could not be deleted");
			}
		}		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
		if (which == -1) // For Positive Button
		{
			TASK = DELETE_NOTE;
			showFullScreenProgresIndicator();
			executeAsyncTask();
		}
		else
		{
			dialog.dismiss();
		}		
	}
}
