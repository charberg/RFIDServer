package com.rfid.system;

import com.google.gson.Gson;

/**
 * 
 * @author Charles Bergeron
 * 
 * This class maps to the items table in the database, and represents an RFID tagged item
 *
 */
public class InventoryItem {

	int itemID;
	String itemName;
	String itemType;
	String description;
	int locationID;
	
	public InventoryItem(int id, String name, String type, String description, int locationID) {
		this.itemID = id;
		this.itemName = name;
		this.itemType = type;
		this.description = description;
		this.locationID = locationID;
	}
	
	public int getID() {
		return itemID;
	}
	public void setID(int itemID) {
		this.itemID = itemID;
	}
	public String getName() {
		return itemName;
	}
	public void setName(String itemName) {
		this.itemName = itemName;
	}
	public String getType() {
		return itemType;
	}
	public void setType(String itemType) {
		this.itemType = itemType;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public int getLocationID() {
		return locationID;
	}
	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}
	
	/**
	 * 
	 * @return JSON String representation of object
	 */
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	
}
