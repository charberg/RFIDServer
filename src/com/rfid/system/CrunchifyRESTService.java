package com.rfid.system;
 
/**
 * @author Crunchify.com
 * 
 */
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import java.sql.SQLException;
import java.util.ArrayList;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
 
/**
 * 
 * @author Charles Bergeron
 *
 * This class represents the REST API, and exposes DB interactions with the RFID inventory management system
 *
 */
@Path("/")
public class CrunchifyRESTService {
	
	DBInterface db;
	
	public CrunchifyRESTService() {
		try {
			db = new DBInterface();
		} catch (ClassNotFoundException e) {
			System.out.println("Problem starting up DB connection");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Problem starting up DB connection");
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles GET requests to get items by ID
	 * @param id - Item ID
	 * @return Response
	 */
	@GET
	@Path("/item/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItemById(@PathParam("id") String id) {

		int itemID = Integer.parseInt(id);
		try {
			InventoryItem item = db.getItemByID(itemID);
			if(item != null) {
				return Response.status(200).entity(item.toJSON()).build();
			}
			else {
				return Response.status(204).entity("Item with requested ID does not exist").build();
			}
		} catch (SQLException e) {
			return Response.status(500).entity("Internal DB error").build();
		}

	}
	
	/**
	 * Handles GET requests to get items by item type
	 * @param type - Requested item type
	 * @return Response
	 */
	@GET
	@Path("/itemByType/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItemsByType(@PathParam("type") String type) {

		try {
			ArrayList<InventoryItem> items = db.getItemsByType(type);
			if(items.size() != 0) {
				
				Gson gson = new Gson();
				
				return Response.status(200).entity(gson.toJson(items)).build();
			}
			else {
				return Response.status(204).entity("No items of this type exist").build();
			}
		} catch (SQLException e) {
			return Response.status(500).entity("Internal DB error").build();
		}

	}
	
	/**
	 * Handles POST requests to update an item's location
	 * @param msg - POST request message
	 * @return Response
	 */
	@POST
    @Path("/post")
    //@Consumes(MediaType.TEXT_XML)
    public Response updateItemLocation(String msg) {
        
        
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(msg).getAsJsonObject();
        try {
        	db.editItemLocation(Integer.parseInt(o.get("itemID").toString()), Integer.parseInt(o.get("locationID").toString()));

			return Response.status(200).entity("Item updated").build();

        } catch (SQLException e) {
			return Response.status(500).entity("Internal DB error").build();
		}
        
    }
	
	/**
	 * Handles GET requests requesting a location by location ID
	 * @param id - Location ID
	 * @return Response
	 */
	@GET
	@Path("/location/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getlocationById(@PathParam("id") String id) {

		int locID = Integer.parseInt(id);
		try {
			Location location = db.getLocationByID(locID);
			if(location != null) {
				return Response.status(200).entity(location.toJSON()).build();
			}
			else {
				return Response.status(204).entity("Item with requested ID does not exist").build();
			}
		} catch (SQLException e) {
			return Response.status(500).entity("Internal DB error").build();
		}

	}
	
	/**
	 * Handles GET requests to verify if system is running
	 * @param incomingData
	 * @return Response
	 */
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "REST RFID Service Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
 
}