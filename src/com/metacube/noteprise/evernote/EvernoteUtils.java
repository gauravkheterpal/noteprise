package com.metacube.noteprise.evernote;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Tag;
import com.metacube.noteprise.common.CommonListItems;
import com.metacube.noteprise.common.Constants;
import com.metacube.noteprise.util.NotepriseLogger;

public class EvernoteUtils 
{
	
	
	public static String stripNoteContent(String noteContent)
	{
		NotepriseLogger.logMessage(noteContent);
		/*int start = 0 , end = noteContent.length() - 1;
    	start = noteContent.indexOf("<en-note><div>") + 14;
    	if (start == -1)
    	{
    		start = noteContent.indexOf("<en-note>") + 9;
    		end = noteContent.indexOf("</en-note>");
    	}
    	else
    	{
    		end = noteContent.indexOf("</div></en-note>");
    	}    	
    	try 
    	{
			noteContent = noteContent.substring(start, end);
			noteContent.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE en-note SYSTEM " +
					"\"http://xml.evernote.com/pub/enml2.dtd\"><en-note>","");
			noteContent.replace("</en-note>", "");
			noteContent.replace("</div>", "");
			noteContent.replace("</div>", "");
			
		} 
    	catch (StringIndexOutOfBoundsException e) 
    	{
    		NotepriseLogger.logError("Exception while stripping note content.", NotepriseLogger.ERROR, e);
		}*/
		noteContent = noteContent.replaceAll("\\<.*?>","");
    	return noteContent;
	}
	
	public static List<Notebook> getAllNotebooks(String authToken, Client client)
	{
		List<Notebook> notebooks = null;
		try 
		{
			notebooks = client.listNotebooks(authToken);
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
		return notebooks;
	}
	
	public static List<Note> getNotesForNotebook(String authToken, Client client, String notebookGuid)
	{
		List<Note> notes = null;
		NoteFilter filter = new NoteFilter();
    	filter.setNotebookGuid(notebookGuid);
    	NoteList noteList;
		try 
		{
			noteList = client.findNotes(authToken, filter, 0, Constants.MAX_NOTES);
			notes = noteList.getNotes();
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
		catch (TException e) 
		{
			e.printStackTrace();
		}	
		return notes;		
	}
	
	public static List<Note> getNotesForCustomNoteFilter(String authToken, Client client, NoteFilter filter)
	{
		List<Note> notes = null;
    	NoteList noteList;
		try 
		{
			noteList = client.findNotes(authToken, filter, 0, Constants.MAX_NOTES);
			notes = noteList.getNotes();
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
		catch (TException e) 
		{
			e.printStackTrace();
		}	
		return notes;		
	}
	
	public static List<CommonListItems> getListItemFromNotesList(String authToken, Client client, List<Note> notes, Notebook notebook)
	{
		ArrayList<CommonListItems> listItems = new ArrayList<CommonListItems>();
		if (notebook != null)
		{
			CommonListItems section = new CommonListItems();
			section.setLabel(notebook.getName());
			section.setTotalContent(notes.size());
			section.setItemType(Constants.ITEM_TYPE_LIST_SECTION);
			section.setId(notebook.getGuid());
			listItems.add(section);	
		}			
    	for (int j=0; j < notes.size(); j++)
    	{
    		CommonListItems item = new CommonListItems();
    		Note note = notes.get(j);
    		item.setLabel(note.getTitle());
    		item.setId(note.getGuid());
    		listItems.add(item);
    	}		
		return listItems;
	}
	
	public static List<CommonListItems> getNotebooksList(String authToken, Client client)
	{
		List<Notebook> notebooks = getAllNotebooks(authToken, client);
		ArrayList<CommonListItems> listItems = new ArrayList<CommonListItems>();		
    	for (int j=0; j < notebooks.size(); j++)
    	{
    		CommonListItems item = new CommonListItems();
    		Notebook nb = notebooks.get(j);
    		item.setLabel(nb.getName());
    		item.setId(nb.getGuid());
    		listItems.add(item);
    	}		
		return listItems;
	}
	
	public static ArrayList<CommonListItems> getAllNotes(String authToken, Client client)
	{
		List<Notebook> notebooks = getAllNotebooks(authToken, client);
		List<Note> notes = null;
		ArrayList<CommonListItems> searchResultItems = new ArrayList<CommonListItems>();
		if (notebooks != null)
		{
			for (int i = 0; i < notebooks.size(); i++)
			{
				Notebook nb = notebooks.get(i);			
				notes = getNotesForNotebook(authToken, client, nb.getGuid());
				List<CommonListItems> temp = getListItemFromNotesList(authToken, client, notes, nb);
				searchResultItems.addAll(temp);
			}
		}				
		return searchResultItems;
	}
	
	public static ArrayList<CommonListItems> searchNotebooks(String authToken, Client client, String queryString)
	{
		List<Notebook> notebooks = getAllNotebooks(authToken, client);
		List<Note> notes = null;
		ArrayList<CommonListItems> searchResultItems = new ArrayList<CommonListItems>();
		if (notebooks != null)
		{
			for (int i = 0; i < notebooks.size(); i++)
			{
				Notebook nb = notebooks.get(i);
				if (nb.getName().contains(queryString))
				{
					notes = getNotesForNotebook(authToken, client, nb.getGuid());
					List<CommonListItems> temp = getListItemFromNotesList(authToken, client, notes, nb);
					searchResultItems.addAll(temp);
				}
			}
		}				
		return searchResultItems;
	}
	
	public static Tag getTagDetails(String authToken, Client client, String tagGuid)
	{
		try 
		{
			return client.getTag(authToken, tagGuid);
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
		catch (TException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<CommonListItems> getListItemFromNotesListForTag(String authToken, Client client, List<Note> notes, Notebook notebook, String tag)
	{
		ArrayList<CommonListItems> listItems = new ArrayList<CommonListItems>();		
		CommonListItems section = new CommonListItems();
		section.setLabel(notebook.getName());
		section.setItemType(Constants.ITEM_TYPE_LIST_SECTION);
		section.setId(notebook.getGuid());
		listItems.add(section);		
    	for (int j=0; j < notes.size(); j++)
    	{
    		CommonListItems item = new CommonListItems();
    		Note note = notes.get(j);
    		List<String> tagList = note.getTagGuids();
    		Boolean matchFound = Boolean.FALSE;
    		if (tagList != null)
    		{
    			for (int k = 0; k < tagList.size(); k++)
        		{
    				Tag tagObject = getTagDetails(authToken, client, tagList.get(k));
    				if (tag != null)
    				{
    					String tagName = tagObject.getName();
            			if(tagName.contains(tag))
            			{
            				matchFound = Boolean.TRUE;
            	    		break;
            			}
    				}    				
        		}
    		}    		
    		if (matchFound)
    		{
    			item.setLabel(note.getTitle());
	    		item.setId(note.getGuid());
	    		listItems.add(item);
    		}
    	}	
    	listItems.get(0).setTotalContent(listItems.size() - 1);
		return listItems;
	}
	
	public static ArrayList<CommonListItems> searchTags(String authToken, Client client, String queryString)
	{
		List<Notebook> notebooks = getAllNotebooks(authToken, client);
		if (notebooks != null)
		{
			List<Note> notes = null;
			ArrayList<CommonListItems> searchResultItems = new ArrayList<CommonListItems>();
			for (int i = 0; i < notebooks.size(); i++)
			{
				Notebook nb = notebooks.get(i);
				notes = getNotesForNotebook(authToken, client, nb.getGuid());
				List<CommonListItems> temp = getListItemFromNotesListForTag(authToken, client, notes, nb, queryString);
				if (temp.size() > 1)
				{
					searchResultItems.addAll(temp);
				}			
			}
			return searchResultItems;
		}
		return null;
		
	}
	
	public static ArrayList<CommonListItems> searchKeywords(String authToken, Client client, String queryString)
	{
		NoteFilter keywordFilter = new NoteFilter();
		keywordFilter.setWords(queryString);
		List<Note> notes = getNotesForCustomNoteFilter(authToken, client, keywordFilter);
		ArrayList<CommonListItems> searchResultItems = (ArrayList<CommonListItems>) getListItemFromNotesList(authToken, client, notes, null);		
		return searchResultItems;
	}
	
	public static Integer deleteNote(String authToken, Client client, String noteGuid)
	{
		try 
		{			
			return client.deleteNote(authToken, noteGuid);
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
		catch (TException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
