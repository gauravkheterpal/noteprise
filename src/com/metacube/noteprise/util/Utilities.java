package com.metacube.noteprise.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import com.metacube.noteprise.common.Constants;

import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;

public class Utilities 
{
	public static File getTempStorageDirectory() 
	{
	    return new File(Environment.getExternalStorageDirectory(), Constants.APP_DATA_PATH);
	}
	
	public static String getStringFromBundle(Bundle bundle, String identifier)
	{
		String bundleContents = "";
		try 
		{			
			bundleContents = bundle.getString(identifier);		
		} 
		catch (Exception e) 
		{
			NotepriseLogger.logError("Exception getting arguements.", NotepriseLogger.WARNING, e);
		}
		return bundleContents;
	}
	
	public static Boolean verifyStringData(String value) 
	{
		if (value != null) 
		{
			value = value.trim();
			if (!value.equalsIgnoreCase("null") && !value.equalsIgnoreCase("")) 
			{
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public static String formatDate(String dateString, String outputFormat, String inputFormat)
	{
		try 
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat);
			return DateFormat.format(outputFormat, dateFormat.parse(dateString)).toString();
		} 
		catch (Exception e) 
		{
			NotepriseLogger.logError("Error formatting date.", NotepriseLogger.ERROR, e);
			return dateString;
		}
	}		
	
	public static String convertStreamToString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try 
		{
			while ((line = reader.readLine()) != null) 
			{
				sb.append(line + "\n");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				is.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	// Stream copier with 1KB buffer.
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
              {
            	  break;
              }                  
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex)
        {
        	NotepriseLogger.logError("Exception in Stream Copier", NotepriseLogger.WARNING, ex);
        }
    }
}
