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