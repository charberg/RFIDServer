package com.rfid.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DBInterface {

	private final String dbName = "test.db";
	private final String testName = "C:\\Users\\puih123\\git\\RFIDSystem\\test.db";
	private final String tableSetupScript = "C:\\Users\\puih123\\git\\RFIDSystem\\db\\tableSetup.sql";
	private final String itemSchema = "(id, name, type, description, location)";
	private final String locationSchema = "(id, name, description, address, lat, lon)";
	private Connection c;
	
	public DBInterface() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:" + testName);
		setupTables();
	}
	
	/**
	 * This function parses a file, and returns the containing text
	 * @param fileName
	 * @return SQL string
	 */
	public String fileToSQLScript(String fileName) {
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = in.readLine()) != null) {
				sb.append(str + "\n ");
			}
			in.close();
			return sb.toString();
		}
		catch (Exception e) {
			System.out.println("Error reading input script " + fileName + ": " + e.getMessage());
		}
		
		return null;
		
	}
	
	public void setupTables() throws SQLException {
		Statement s = c.createStatement();
		s.executeUpdate(fileToSQLScript(tableSetupScript));
	}
	
	public void addItem(InventoryItem i) throws SQLException {
		
		String sql = "INSERT INTO items" + itemSchema + String.format(" VALUES(%d, '%s', '%s', '%s', %d)", i.getID(),
																									i.getName(),
																									i.getType(),
																									i.getDescription(),
																									i.getLocationID());
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	public void addLocation(Location l) throws SQLException {
		
		String sql = "INSERT INTO locations" + locationSchema + String.format(" VALUES(%d, '%s', '%s', '%s', '%s', '%s')", l.getID(),
																									l.getName(),
																									l.getDescription(),
																									l.getAddress(),
																									l.getLat(),
																									l.getLon());
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	public void editItemLocation(int itemID, int locationID) throws SQLException {
		
		String sql = String.format("UPDATE items SET location=%d WHERE id=%d", locationID, itemID);
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	public void editLocationCoordinates(int locationID, String lat, String lon) throws SQLException {
		
		String sql = String.format("UPDATE locations SET lat='%s', lon='%s' WHERE id=%d", lat, lon, locationID);
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	public void editLocationAddress(int locationID, String address) throws SQLException {
		
		String sql = String.format("UPDATE locations SET address='%s' WHERE id=%d", address, locationID);
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	//BE CAREFUL.
	public void deleteItem(int itemID) throws SQLException {
		
		String sql = String.format("DELETE FROM items WHERE id=%d", itemID);
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	//BE CAREFUL.
	public void deleteLocation(int id) throws SQLException {
		
		String sql = String.format("DELETE FROM locations WHERE id=%d", id);
		Statement s = c.createStatement();
		s.executeUpdate(sql);
		
	}
	
	public InventoryItem getItemByID(int id) throws SQLException {
		
		String sql = String.format("SELECT * FROM items WHERE id=%d", id);
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery(sql);
		
		if(r.next()) {
		
			return new InventoryItem(r.getInt("id"),
								r.getString("name"),
								r.getString("type"),
								r.getString("description"),
								r.getInt("location"));
		}
		
		//No result found
		return null;
		
	}
	
	//Note we may want to auto call this when fetching a location object by ID, to populate it's current items field.
	//If not, will need to be properly documented
	public ArrayList<InventoryItem> getItemsByLocationID(int id) throws SQLException {
		
		String sql = String.format("SELECT * FROM items WHERE location=%d", id);
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery(sql);
		
		ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
		
		if(r.next()) {
		
			items.add(new InventoryItem(r.getInt("id"),
								r.getString("name"),
								r.getString("type"),
								r.getString("description"),
								r.getInt("location")));
		}
		
		//No result found
		return items;
		
	}
	
	public Location getLocationByID(int id) throws SQLException {
		
		String sql = String.format("SELECT * FROM locations WHERE id=%d", id);
		Statement s = c.createStatement();
		ResultSet r = s.executeQuery(sql);
		
		if(r.next()) {
		
			Location l = new Location(r.getInt("id"),
								r.getString("name"),
								r.getString("description"),
								r.getString("address"),
								r.getString("lat"),
								r.getString("lon"));
			
			l.setCurrentItems(this.getItemsByLocationID(l.getID()));
			
			return l;
			
		}
		
		//No result found
		return null;
		
	}
	
	/*public static void main( String args[] ) throws ClassNotFoundException, SQLException
	  {
		DBInterface db = new DBInterface();
		
		
		
		InventoryItem i = new InventoryItem(2, "Ball Peen Hammer", "Hammer", "Hammer, but peen", 1);
		Location l = new Location(2, "The Moon", "Its the moon", "1 Moon lane", "latTest", "lonTest");
		
		db.addItem(i);
		db.addLocation(l);
		System.out.println("debug");
		
	  }*/
	
}
