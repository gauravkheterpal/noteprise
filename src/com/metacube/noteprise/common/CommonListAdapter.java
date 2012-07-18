package com.metacube.noteprise.common;

import java.util.ArrayList;
import java.util.Collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.metacube.noteprise.R;
import com.metacube.noteprise.util.CommonListComparator;

public class CommonListAdapter extends BaseAdapter 
{
	public ArrayList<CommonListItems> listItems = null;
	int count;
	LayoutInflater inflater = null;
	View listItemLayout = null;
	TextView listItemMainTextView = null;
	ImageView leftImageView = null;
	Boolean isCheckListMode = Boolean.FALSE;

	public CommonListAdapter(LayoutInflater inflater, ArrayList<CommonListItems> listItems) 
	{
		this.listItems = listItems;
		this.count = listItems.size();
		this.inflater = inflater;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		CommonListItems item = listItems.get(position);
		if (item.getItemType().equalsIgnoreCase(Constants.ITEM_TYPE_LIST_SECTION))
		{
			listItemLayout = inflater.inflate(R.layout.list_section_layout, parent, false);
			listItemMainTextView = (TextView) listItemLayout.findViewById(R.id.list_section_item_text_view);
			String sectionTitle = item.getLabel();
			if (item.getTotalContent() != null)
			{
				sectionTitle = sectionTitle + "   (" + item.getTotalContent() + ")";
			}
			listItemMainTextView.setText(sectionTitle);			
		}
		else
		{
			listItemLayout = inflater.inflate(R.layout.common_list_item_layout, parent, false);
			listItemMainTextView = (TextView) listItemLayout.findViewById(R.id.list_item_main_text);
			leftImageView = (ImageView) listItemLayout.findViewById(R.id.list_item_left_image);		
			listItemMainTextView.setText(item.getLabel());
			if (item.getLeftImage() != null && !isCheckListMode)
			{
				leftImageView.setImageResource(item.getLeftImage());
				leftImageView.setVisibility(View.VISIBLE);
			}
			else if (isCheckListMode)
			{
				if (item.getIsChecked())
				{
					leftImageView.setImageResource(R.drawable.button_checked);
				}
				else
				{
					leftImageView.setImageResource(R.drawable.button_unchecked);
				}			
				leftImageView.setVisibility(View.VISIBLE);
			}
		}
		
		return listItemLayout;
	}

	@Override
	public int getCount() 
	{
		this.count = listItems.size();
		return count;
	}
	
	public String getListItemText(int position)
	{
		return listItems.get(position).getName();
	}
	
	public String getListItemId(int position)
	{
		return listItems.get(position).getId();
	}
	
	public String getSortData(int position)
	{
		return listItems.get(position).getSortData();
	}
	
	public String getTag(int position)
	{
		return listItems.get(position).getTag();
	}

	@Override
	public Object getItem(int position) 
	{
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	public Boolean isListItem(int position)
	{
		if (listItems.get(position).getItemType().equalsIgnoreCase(Constants.ITEM_TYPE_LIST_SECTION))
		{
			return false;
		}
		return true;
	}
	
	public void showCheckList()
	{
		isCheckListMode = Boolean.TRUE;
		notifyDataSetChanged();
	}
	
	public Boolean isItemChecked(int position)
	{
		return listItems.get(position).getIsChecked();
	}
	
	public void setChecedkCurrentItem(int position)
	{
		if (isItemChecked(position))
		{
			listItems.get(position).setLeftImage(R.drawable.button_unchecked);
			listItems.get(position).setIsChecked(Boolean.FALSE);
		}
		else
		{
			listItems.get(position).setLeftImage(R.drawable.button_checked);
			listItems.get(position).setIsChecked(Boolean.TRUE);
		}		
		notifyDataSetChanged();
	}
	
	public Boolean isCheckListMode()
	{
		return isCheckListMode;
	}
	
	public ArrayList<String> getCheckedItemsList()
	{
		ArrayList<String> checkedList = new ArrayList<String>();
		for (int i = 0; i < listItems.size(); i++)
		{
			if(listItems.get(i).getIsChecked())
			{
				checkedList.add(listItems.get(i).getId());
			}
		}
		return checkedList;
	}
	
	public void changeOrdering(String orderType)
	{
		// Sort By Name
		if(orderType.equalsIgnoreCase(Messages.SORT_BY_NAME))
		{
			Collections.sort(listItems, new CommonListComparator(CommonListComparator.COMPARE_BY_NAME));
		}
		// Sort By Date
		else if(orderType.equalsIgnoreCase(""))
		{
			Collections.sort(listItems, new CommonListComparator(CommonListComparator.COMPARE_BY_SORT_DATA));
		}
		// Sort By id
		else if (orderType.equalsIgnoreCase(Messages.SORT_BY_ID))
		{
			Collections.sort(listItems, new CommonListComparator(CommonListComparator.COMPARE_BY_ID));
		}
		// By default sort by Label
		else
		{
			Collections.sort(listItems, new CommonListComparator(CommonListComparator.COMPARE_BY_LABEL));
		}
		notifyDataSetChanged();
	}
}