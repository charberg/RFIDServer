package com.rfid.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * @author Charles Bergeron
 * 
 * This class is to be used as an interface to the RFID items/locations database, so that queries do not need to be manually written by users.
 *
 */
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
	
	/**
	 * Run the setup SQL file, if your db/tables do not exist yet.
	 * Should probably be run on initialization to be safe.
	 * @throws SQLException
	 */
	public void setupTables() throws SQLException {
		Statement s = c.createStatement();
		s.executeUpdate(fileToSQLScript(tableSetupScript));
	}
	
	/**
	 * Add an InventoryItem to the database.
	 * This should be used when adding a newly tagged object, NOT when updating an object's location or attributes
	 * @param i - InventoryItem to be added
	 * @throws SQLException
	 */
	public void addItem(InventoryItem i) throws SQLException {
		
		String sql = "INSERT INTO items" + itemSchema + " VALUES(?, ?, ?, ?, ?)";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, i.getID());
		s.setString(2, i.getName());
		s.setString(3, i.getName());
		s.setInt(4, i.getLocationID());
		s.executeUpdate();
		
	}
	
	/**
	 * This should be used to add a new location to the databased, not to update an item's location, or to modify a location's attributes
	 * @param l - Location to be added
	 * @throws SQLException
	 */
	public void addLocation(Location l) throws SQLException {
		
		String sql = "INSERT INTO locations" + locationSchema + " VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, l.getID());
		s.setString(2, l.getName());
		s.setString(3, l.getDescription());
		s.setString(4, l.getAddress());
		s.setString(5, l.getLat());
		s.setString(6, l.getLon());
		s.executeUpdate(sql);
		
	}
	
	/**
	 *  This should be used to update an item's location to where it is currently being used/stored
	 * @param itemID - ID of the item to be changed
	 * @param locationID - ID of the location at which the item is now located
	 * @throws SQLException
	 */
	public void editItemLocation(int itemID, int locationID) throws SQLException {
		
		String sql = "UPDATE items SET location=? WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, locationID);
		s.setInt(2, itemID);
		s.executeUpdate();
		
	}
	
	/**
	 * Updates a location's co-ordinates.
	 * Useful for mobile locations such as work vans.
	 * @param locationID - ID of the location to be updated
	 * @param lat - Latitude
	 * @param lon - Longitude
	 * @throws SQLException
	 */
	public void editLocationCoordinates(int locationID, String lat, String lon) throws SQLException {
		
		String sql = "UPDATE locations SET lat=?, lon=? WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setString(1, lat);
		s.setString(2, lon);
		s.setInt(3, locationID);
		s.executeUpdate();
		
	}
	
	/**
	 * Updates a location's address
	 * Useful for when a physical location moves locations, such as an office move. This could also be used for a van's current work location if desired.
	 * @param locationID - ID of the location to be updated
	 * @param address - New address of the location
	 * @throws SQLException
	 */
	public void editLocationAddress(int locationID, String address) throws SQLException {
		
		String sql = "UPDATE locations SET address=? WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setString(1, address);
		s.setInt(2, locationID);
		s.executeUpdate();
		
	}
	
	/**
	 * Permanently delete an item from the database.
	 * This should not be reflected in an external API.
	 * @param itemID
	 * @throws SQLException
	 */
	//BE CAREFUL.
	public void deleteItem(int itemID) throws SQLException {
		
		String sql = "DELETE FROM items WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, itemID);
		s.executeUpdate();
		
	}
	
	/**
	 * Permanently delete a location from the database.
	 * This should not be reflected in an external API.
	 * @param itemID
	 * @throws SQLException
	 */
	//BE CAREFUL.
	public void deleteLocation(int id) throws SQLException {
		
		String sql = "DELETE FROM locations WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, id);
		s.executeUpdate();
		
	}
	
	/**
	 * Get item by item ID
	 * @param id - Item's ID
	 * @return InventoryItem
	 * @throws SQLException
	 */
	public InventoryItem getItemByID(int id) throws SQLException {
		
		String sql = "SELECT * FROM items WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, id);
		ResultSet r = s.executeQuery();
		
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
	
	/**
	 * Get all items with a specified item type
	 * @param type - Requested item type
	 * @return - ArrayList<InventoryItem>
	 * @throws SQLException
	 */
	public ArrayList<InventoryItem> getItemsByType(String type) throws SQLException {
		
		String sql = "SELECT * FROM items WHERE type=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setString(1, type);
		ResultSet r = s.executeQuery();
		
		ArrayList<InventoryItem> results = new ArrayList<InventoryItem>();
		
		if(r.next()) {
		
			results.add(new InventoryItem(r.getInt("id"),
								r.getString("name"),
								r.getString("type"),
								r.getString("description"),
								r.getInt("location")));
		}
		
		return results;
		
	}

	/**
	 * Get all items with corresponding location ID
	 * @param id - Location's ID
	 * @return ArrayList<InventoryItem>
	 * @throws SQLException
	 */
	//Note we may want to auto call this when fetching a location object by ID, to populate it's current items field.
	//If not, will need to be properly documented
	public ArrayList<InventoryItem> getItemsByLocationID(int id) throws SQLException {
		
		String sql = "SELECT * FROM items WHERE location=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, id);
		ResultSet r = s.executeQuery();
		
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
	
	/**
	 * Get location by unique ID
	 * @param id - Location's ID
	 * @return Location
	 * @throws SQLException
	 */
	public Location getLocationByID(int id) throws SQLException {
		
		String sql = "SELECT * FROM locations WHERE id=?";
		PreparedStatement s = c.prepareStatement(sql);
		s.setInt(1, id);
		ResultSet r = s.executeQuery();
		
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
	
}
