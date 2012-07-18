package com.metacube.noteprise.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;

import com.evernote.edam.notestore.NoteStore.Client;

public class CommonMessageDialog implements OnClickListener
{
	Builder alertDialogBuilder;
	public AlertDialog messageDialog;
	Context context;
	Handler dialogButtonHandler = null;
	
	public CommonMessageDialog(Context context) 
	{
		this.context = context;
	}
	
	public Boolean isAlreadyShowing() 
	{
		if (messageDialog != null) 
		{
			if (messageDialog.isShowing()) 
			{
				return true;
			}
		}
		return false;
	}
	
	public void showMessageDialog(String message)
	{
		if (isAlreadyShowing())
		{
			return;
		}
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setNeutralButton("Ok", this);
		messageDialog = alertDialogBuilder.create();
		messageDialog.show();
	}
	
	public void showMessageDialog(String message, String buttonText)
	{
		if (isAlreadyShowing())
		{
			return;
		}
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setNeutralButton(buttonText, this);
		messageDialog = alertDialogBuilder.create();
		messageDialog.show();
	}
	
	public void showFinishActivityDialog(String message)
	{
		if (isAlreadyShowing())
		{
			return;
		}
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setNeutralButton("Ok",
				new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					dialog.dismiss();
					((Activity) context).finish();
				}
			});
		messageDialog = alertDialogBuilder.create();
		messageDialog.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int id) 
	{
		dialog.dismiss();
	}
	
	public void showChangeActivityDialog(final Intent intent, String message)
	{
		if (isAlreadyShowing())
		{
			return;
		}
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setNeutralButton("Ok", 
				new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
						context.startActivity(intent);
					}
				});
		messageDialog = alertDialogBuilder.create();
		messageDialog.show();
	}
	
	public void showDeleteNoteDialog(String authToken, Client client, OnClickListener listener)
	{
		if (isAlreadyShowing())
		{
			return;
		}
		alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage("Are you sure you want to delete this note?");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("Yes", listener);
		alertDialogBuilder.setNegativeButton("No", listener);
		messageDialog = alertDialogBuilder.create();
		messageDialog.show();
	}
}