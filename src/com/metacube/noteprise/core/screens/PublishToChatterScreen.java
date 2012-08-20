package com.metacube.noteprise.core.screens;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.ParseException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metacube.noteprise.R;
import com.metacube.noteprise.common.BaseFragment;
import com.metacube.noteprise.util.NotepriseLogger;
import com.metacube.noteprise.util.Utilities;
import com.salesforce.androidsdk.rest.RestClient.AsyncRequestCallback;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestRequest.RestMethod;
import com.salesforce.androidsdk.rest.RestResponse;

public class PublishToChatterScreen extends BaseFragment implements AsyncRequestCallback, OnClickListener
{
	String noteContent;
	RestResponse publishResponse = null;
	Button publishToChatterButton;
	TextView chatterNoteContent;
	
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
    	View contentView = inflater.inflate(R.layout.publish_to_chatter_screen_layout, container);    	
    	publishToChatterButton = (Button) contentView.findViewById(R.id.publish_to_chatter_button);
    	chatterNoteContent = (TextView) contentView.findViewById(R.id.chatter_note_content);
    	publishToChatterButton.setOnClickListener(this);
    	return super.onCreateView(inflater, container, savedInstanceState);
    }
	
	@Override
	public void onResume() 
	{
		super.onResume();
		chatterNoteContent.setText(noteContent);	
	}
	
	@Override
	public void onTaskStarted() 
	{
		super.onTaskStarted();
		
		if (salesforceRestClient != null)
		{
			try 
			{
				String encodedText = URLEncoder.encode(noteContent);		
				publishResponse = salesforceRestClient.sendSync(RestMethod.POST, "/services/data/" + SF_API_VERSION + "/chatter/feeds/news/me/feed-items?text=" + encodedText, null);
			} 
			catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}		
		}
	}
	
	public void publishNoteToChatterFeed()
	{
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
		if (publishResponse != null)
		{
			try 
			{
				NotepriseLogger.logMessage(publishResponse.asString());
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
	}
}