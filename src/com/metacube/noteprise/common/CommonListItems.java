package com.metacube.noteprise.common;

public class CommonListItems 
{
	private String id = null;
	private String name = null;
	private String label = null;
	private String sortData = null;
	private String tag = null;
	private Boolean isChecked = Boolean.FALSE;
	private Integer leftImage = null;
	private String leftImageURL = null;
	private String itemType = Constants.ITEM_TYPE_LIST_ITEM;
	private Integer totalContent = null;
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getLabel() {
		return label;
	}
	public String getSortData() {
		return sortData;
	}
	public String getTag() {
		return tag;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public Integer getLeftImage() {
		return leftImage;
	}
	public String getLeftImageURL() {
		return leftImageURL;
	}
	public String getItemType() {
		return itemType;
	}
	public Integer getTotalContent() {
		return totalContent;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setSortData(String sortData) {
		this.sortData = sortData;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	public void setLeftImage(Integer leftImage) {
		this.leftImage = leftImage;
	}
	public void setLeftImageURL(String leftImageURL) {
		this.leftImageURL = leftImageURL;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public void setTotalContent(Integer totalContent) {
		this.totalContent = totalContent;
	}	
}