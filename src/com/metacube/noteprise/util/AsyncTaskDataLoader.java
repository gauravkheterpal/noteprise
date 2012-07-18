package com.metacube.noteprise.util;

import com.metacube.noteprise.common.BaseActivity;
import com.metacube.noteprise.common.BaseFragment;

import android.os.AsyncTask;

public class AsyncTaskDataLoader extends AsyncTask<Void, Void, Void> 
{
	BaseActivity baseActivity = null;
	BaseFragment baseFragment = null;
	
	public AsyncTaskDataLoader(BaseActivity baseActivity) 
	{
		this.baseActivity = baseActivity;
	}
	
	public AsyncTaskDataLoader(BaseFragment baseFragment) 
	{
		this.baseFragment = baseFragment;
	}
	
	@Override
	protected void onPreExecute() 
	{
		super.onPreExecute();
		if (baseFragment != null)
		{
			baseFragment.onTaskStarted();
		}
		else
		{
			baseActivity.onTaskStarted();
		}		
	}
	
	@Override
	protected Void doInBackground(Void... params) 
	{
		if (baseFragment != null)
		{
			baseFragment.doTaskInBackground();
		}
		else
		{
			baseActivity.doTaskInBackground();
		}		
		return null;
	}
	
	@Override
	protected void onCancelled() 
	{
		super.onCancelled();
		if (baseFragment != null)
		{
			baseFragment.onTaskCancelled();
		}
		else
		{
			baseActivity.onTaskCancelled();
		}		
	}
	
	@Override
	protected void onProgressUpdate(Void... values) 
	{
		super.onProgressUpdate(values);
		if (baseFragment != null)
		{
			baseFragment.onTaskUpdate();
		}
		else
		{
			baseActivity.onTaskUpdate();
		}		
	}
	
	@Override
	protected void onPostExecute(Void result) 
	{
		super.onPostExecute(result);
		if (baseFragment != null)
		{
			baseFragment.onTaskFinished();
		}
		else
		{
			baseActivity.onTaskFinished();
		}		
	}
}