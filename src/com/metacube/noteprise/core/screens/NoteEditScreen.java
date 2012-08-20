package com.metacube.noteprise.core.screens;

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
import android.widget.LinearLayout;

import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;

public class NoteEditScreen extends BaseFragment implements OnClickListener, android.content.DialogInterface.OnClickListener, OnTouchListener
{
	String authToken;
	Client client;
	WebView noteContentWebView;
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
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	clearContainer(container);
    	View contentView = inflater.inflate(R.layout.note_detail_screen_layout, container);    	
    	noteContentWebView = (WebView) contentView.findViewById(R.id.note_content_web_view);
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
		
	}
	
	@Override
	public void onResume() 
	{
		super.onResume();
		
	}
	
	@Override
	public void onStop() 
	{
		super.onStop();
		
	}
	
	@Override
	public void doTaskInBackground() 
	{
		super.doTaskInBackground();
		
	}
	
	@Override
	public void onTaskFinished() 
	{
		super.onTaskFinished();
		
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