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
	
	@POST
	@Path("/crunchifyService")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + crunchifyBuilder.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(crunchifyBuilder.toString()).build();
	}
 
	@GET
	@Path("/item")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItemRestService(InputStream incomingData) {
		InventoryItem item = new InventoryItem(1, "Eaglehorn Bow", "Bow", "Gains +1 durability per secret", 420);
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(item.toJSON()).build();
	}
	
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
	
	@GET
	@Path("/location")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationRestService(InputStream incomingData) {
		InventoryItem item = new InventoryItem(1, "Eaglehorn Bow", "Bow", "Gains +1 durability per secret", 420);
		InventoryItem item2 = new InventoryItem(1, "Gladiator Bow", "Bow", "Immune while attacking", 360);
		Location loc = new Location(420, "Arena", "RNG test", "123 Brode Lane", "Haha", "Hoho");
		ArrayList<InventoryItem> i = new ArrayList<InventoryItem>();
		i.add(item);
		i.add(item2);
		loc.setCurrentItems(i);
		
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(loc.toJSON()).build();
	}
	
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
	
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "CrunchifyRESTService Successfully started..";
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
 
}