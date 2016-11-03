package com.rfid.system;

import java.util.ArrayList;

import com.google.gson.Gson;


public class Location {

	int locationID;
	String locationName;
	String description;
	String address;
	String lat;
	String lon;
	ArrayList<InventoryItem> currentItems;
	
	public Location(int id, String name, String description, String address, String lat, String lon) {
		this.locationID = id;
		this.locationName = name;
		this.description = description;
		this.address = address;
		this.lat = lat;
		this.lon = lon;
		
		//Null means items havent been initialized yet, will need to do db call. Could be done automatically in constructor, but dont think so.
		//Maybe the getter should make the call, so it's accurate at time of get call?
		currentItems = new ArrayList<InventoryItem>();
	}
	
	public int getID() {
		return locationID;
	}
	public void setID(int locationID) {
		this.locationID = locationID;
	}
	public String getName() {
		return locationName;
	}
	public void setName(String locationName) {
		this.locationName = locationName;
	}
	
	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public ArrayList<InventoryItem> getCurrentItems() {
		return currentItems;
	}
	public void setCurrentItems(ArrayList<InventoryItem> currentItems) {
		this.currentItems = currentItems;
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
