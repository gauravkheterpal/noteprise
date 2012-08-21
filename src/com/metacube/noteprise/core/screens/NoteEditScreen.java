package com.metacube.noteprise.core.screens;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.evernote.EvernoteUtils;
import com.metacube.noteprise.util.NotepriseLogger;
import com.metacube.noteprise.util.Utilities;
import com.metacube.noteprise.util.richtexteditor.Html;

public class NoteEditScreen extends BaseFragment implements OnClickListener, android.content.DialogInterface.OnClickListener, OnTouchListener
{
	String authToken;
	Client client;
	WebView noteContentWebView;
	LinearLayout updateButton;
	String noteTitle, noteContent, noteGuid;
	Note note;
	EditText noteTitleEditText, noteContenteditText;
	int GET_DATA = 0, SAVE_DATA = 1, CURRENT_TASK = 0;
	Integer GET_NOTE_DATA = 0, UPDATE_NOTE = 1, TASK = 0, deletionId = null;
	String saveString;
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
        
       // noteContent = Utilities.getStringFromBundle(args, "noteContent");
       // saveString = EvernoteUtils.stripEvernoteSuffixAndPrefix(noteContent);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	 clearContainer(container);
    	 View contentView = inflater.inflate(R.layout.note_edit_screen_layout, container);
    	 noteContenteditText = (EditText) contentView.findViewById(R.id.content);
    	 updateButton = (LinearLayout) addViewToBaseHeaderLayout(inflater, R.layout.common_update_button_layout, R.id.common_update_button);
    	 updateButton.setVisibility(View.VISIBLE);
    	 updateButton.setOnClickListener(this);
    	//noteContentWebView = (WebView) contentView.findViewById(R.id.note_content_web_view);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onStart() 
    {
    	super.onStart();
    	
    }

	@Override
	public void onClick(View view) 
	{
		if(view == updateButton)
		{ 
			String content = noteContenteditText.getText().toString().trim();
			NotepriseLogger.logMessage(content);
			TASK = UPDATE_NOTE;
			showFullScreenProgresIndicator();
			executeAsyncTask();
	
		}
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		TASK = GET_NOTE_DATA;
        showFullScreenProgresIndicator();
		executeAsyncTask();
		
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		removeViewFromBaseHeaderLayout(updateButton);
		
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
		}	else if (TASK == UPDATE_NOTE)
		{
			EvernoteUtils.updateNote(authToken, client, note);
		}
		
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		if (TASK == GET_NOTE_DATA)
		{
			hideFullScreenProgresIndicator();			
	    	noteTitle = note.getTitle();
	    	noteContent = note.getContent();
	    	
	    	//saveString = EvernoteUtils.stripEvernoteSuffixAndPrefix(noteContent);//EvernoteUtils.stripNoteContent(note.getContent());
	    	noteContenteditText.setText(Html.fromHtml(noteContent).toString());
	    	
		}
		else if (TASK == UPDATE_NOTE)
		{
			note.setContent(Html.toHtml(noteContenteditText.getText()));
			hideFullScreenProgresIndicator();	
			finishScreen();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
				
	}
	
	@Override
    public boolean onTouch(View view, MotionEvent event) 
    {
        switch (event.getAction()) 
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                if (!view.hasFocus()) 
                {
                    view.requestFocus();
                }
                break;
        }
        return false;
    }
}