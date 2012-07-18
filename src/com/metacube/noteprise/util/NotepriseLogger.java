package com.metacube.noteprise.util;

import com.metacube.noteprise.common.Constants;
import com.metacube.noteprise.common.Messages;

import android.util.Log;

public class NotepriseLogger 
{
	public static Boolean LOGGING_ENABLED		=	Constants.DEBUGGING_ENABLED;
	public static Boolean PRINT_STACK_TRACE		=	Constants.STACKTRACE_ENABLED;
	
	public static final int VERBOSE	=	0;
	public static final int ERROR	=	1;
	public static final int WARNING	=	2;
	public static final int DEBUG	=	3;
	public static final int INFO	=	4;
	
	public static void logMessage(String message)
	{
		if(LOGGING_ENABLED)
		{
			Log.v(Messages.LOG_TAG, message);
		}		
	}
	
	public static void logError(String message, int logType)
	{
		if (LOGGING_ENABLED) 
		{
			switch (logType) 
			{
				case VERBOSE: 
				{
					Log.v(Messages.LOG_TAG, message);
					break;
				}
				case ERROR: 
				{
					Log.e(Messages.LOG_TAG, message);
					break;
				}
				case WARNING: 
				{
					Log.w(Messages.LOG_TAG, message);
					break;
				}
				case DEBUG: 
				{
					Log.d(Messages.LOG_TAG, message);
					break;
				}
				case INFO:
				{
					Log.i(Messages.LOG_TAG, message);
					break;
				}				
				default:
				{
					Log.v(Messages.LOG_TAG, message);
				}
			}
		}
	}
	
	public static void logError(String message, int logType, Exception exception)
	{
		if (LOGGING_ENABLED) 
		{
			switch (logType) 
			{
				case VERBOSE: 
				{
					Log.v(Messages.LOG_TAG, message);
					break;
				}
				case ERROR: 
				{
					Log.e(Messages.LOG_TAG, message);
					break;
				}
				case WARNING: 
				{
					Log.w(Messages.LOG_TAG, message);
					break;
				}
				case DEBUG: 
				{
					Log.d(Messages.LOG_TAG, message);
					break;
				}
				case INFO:
				{
					Log.i(Messages.LOG_TAG, message);
					break;
				}				
				default:
				{
					Log.v(Messages.LOG_TAG, message);
				}
			}
			if (PRINT_STACK_TRACE) 
			{
				exception.printStackTrace();
			}			
		}
	}

}
