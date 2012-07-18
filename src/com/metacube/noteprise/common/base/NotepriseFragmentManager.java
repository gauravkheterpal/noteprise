package com.metacube.noteprise.common.base;

import java.util.ArrayList;

import com.metacube.noteprise.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


public class NotepriseFragmentManager
{
	FragmentActivity fragmentActivity;
	int ROOT_CONTAINER_ID = R.id.fragment_view_frame_layout;
	ArrayList<NotepriseFragment> screenStack;
	
	public NotepriseFragmentManager(FragmentActivity fragmentActivity) 
	{
		this.fragmentActivity = fragmentActivity;
		this.screenStack = new ArrayList<NotepriseFragment>();
	}
	
	public void changeScreen(NotepriseFragment npFragment)
	{
		FragmentTransaction ft = initFragmentTransaction();
		if (screenStack.size() != 0)
		{
			ft.detach(screenStack.get(screenStack.size() -1)._fragment);
		}
		npFragment._fragment = Fragment.instantiate(fragmentActivity, npFragment._class.getName(), npFragment._args);
		ft.add(ROOT_CONTAINER_ID, npFragment._fragment, npFragment._tag);
		screenStack.add(npFragment);
		//ft.addToBackStack(npFragment._tag); 
		ft.commit();
		fragmentActivity.getSupportFragmentManager().executePendingTransactions();
	}
	
	public FragmentTransaction initFragmentTransaction() 
	{
		FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();	
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
		fragmentTransaction.disallowAddToBackStack();
		return fragmentTransaction;
	}
	
	public Boolean onBackPressed()
	{
		FragmentTransaction ft = initFragmentTransaction();
		if (screenStack.size() > 1)
		{
			ft.remove(screenStack.get(screenStack.size() - 1)._fragment);
			screenStack.remove(screenStack.size() - 1);
			ft.attach(screenStack.get(screenStack.size() - 1)._fragment);
			ft.commit();
			fragmentActivity.getSupportFragmentManager().executePendingTransactions();
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	public void clearScreen()
	{
		while(onBackPressed());
	}
}