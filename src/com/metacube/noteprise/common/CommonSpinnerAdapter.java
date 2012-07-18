package com.metacube.noteprise.common;

import java.util.ArrayList;
import java.util.Collections;

import com.metacube.noteprise.R;
import com.metacube.noteprise.util.CommonListComparator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CommonSpinnerAdapter extends BaseAdapter implements SpinnerAdapter 
{
	ArrayList<CommonListItems> items;
	LayoutInflater inflater;
	
	public CommonSpinnerAdapter(LayoutInflater inflater, ArrayList<CommonListItems> items) 
	{
		this.items = items;
		this.inflater = inflater;
	}	

	@Override
	public int getCount() 
	{
		return items.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		//return items.get(position).getId();
		return 0;
	}
	
	public String getSpinnerItemId(int position)
	{
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View layout = inflater.inflate(R.layout.common_spinner_item_layout, null);
		CommonListItems item = items.get(position);
		TextView title = (TextView) layout.findViewById(R.id.spinner_item_label_text);
		TextView subText = (TextView) layout.findViewById(R.id.spinner_item_sub_text);
		title.setText(item.getLabel());
		if (item.getName() != null)
		{
			subText.setText("(" + item.getName() + ")");
			subText.setVisibility(View.VISIBLE);
		}		
		return layout;
	}
	
	public void changeOrdering(String orderType)
	{
		// Sort By Name
		if(orderType.equalsIgnoreCase(Messages.SORT_BY_NAME))
		{
			Collections.sort(items, new CommonListComparator(CommonListComparator.COMPARE_BY_NAME));
		}
		// Sort By Date
		else if(orderType.equalsIgnoreCase(""))
		{
			Collections.sort(items, new CommonListComparator(CommonListComparator.COMPARE_BY_SORT_DATA));
		}
		// Sort By id
		else if (orderType.equalsIgnoreCase(Messages.SORT_BY_ID))
		{
			Collections.sort(items, new CommonListComparator(CommonListComparator.COMPARE_BY_ID));
		}
		// By default sort by Label
		else
		{
			Collections.sort(items, new CommonListComparator(CommonListComparator.COMPARE_BY_LABEL));
		}
		notifyDataSetChanged();
	}
}
