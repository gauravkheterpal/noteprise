package com.metacube.noteprise.util;

import java.util.Comparator;

import com.metacube.noteprise.common.CommonListItems;

public class CommonListComparator implements Comparator<CommonListItems>
{
	public static final int COMPARE_BY_LABEL = 0;
	public static final int COMPARE_BY_ID = 1;
	public static final int COMPARE_BY_NAME = 2;
	public static final int COMPARE_BY_SORT_DATA = 3;
	
	private int compareBy;
	
	public CommonListComparator(int compareBy) 
	{
		this.compareBy = compareBy;
	}

	@Override
	public int compare(CommonListItems lhs, CommonListItems rhs) 
	{
		int result = 0;
		switch (compareBy)
		{
			case COMPARE_BY_LABEL:
			{
				result = lhs.getLabel().compareTo(rhs.getLabel());
				break;
			}
			case COMPARE_BY_ID:
			{
				result = lhs.getId().compareTo(rhs.getId());
				break;
			}			
			case COMPARE_BY_SORT_DATA:
			{
				result = lhs.getSortData().compareTo(rhs.getSortData());
				break;
			}
			case COMPARE_BY_NAME:
			default:
			{
				result = lhs.getName().compareTo(rhs.getName());
				break;
			}
		}		
		return result;
	}	
}