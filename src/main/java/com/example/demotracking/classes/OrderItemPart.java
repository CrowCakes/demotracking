package com.example.demotracking.classes;

public class OrderItemPart {
	private int listID;
	private int itemID;
	private String name;
	private String serial;
	
	public OrderItemPart(int listID, int itemID, String name, String serial) {
		super();
		this.listID = listID;
		this.itemID = itemID;
		this.name = name;
		this.serial = serial;
	}

	public int getListID() {
		return listID;
	}

	public void setListID(int listID) {
		this.listID = listID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	
	public String getListIDStr() {
		return String.valueOf(listID);
	}

	public void setListID(String listID) {
		this.listID = Integer.parseInt(listID);
	}

	public String getItemIDStr() {
		return String.valueOf(itemID);
	}

	public void setItemID(String itemID) {
		this.itemID = Integer.parseInt(itemID);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}
	
	
}
