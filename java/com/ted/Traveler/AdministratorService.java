package com.ted.Traveler;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.ted.Traveler.Database.Calendar;
import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.Listing;
import com.ted.Traveler.Database.Review;
import com.ted.Traveler.Database.User;
import com.ted.Traveler.XmlEntities.Calendars;
import com.ted.Traveler.XmlEntities.Listings;
import com.ted.Traveler.XmlEntities.Reviews;
import com.ted.Traveler.XmlEntities.Users;


@Path("admin")
public class AdministratorService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() throws Exception {
		DatabaseConnection database = new DatabaseConnection();	
		return database.getUsers();
	}
	
	@POST
	@Path("activation")
	public Response activationController(@FormParam("UserID") String UserID, 
										 @FormParam("Activation") String Activation) throws Exception {
		DatabaseConnection database = new DatabaseConnection();	
		User user = null;
		java.net.URI location = new java.net.URI("http://localhost:8080/Traveler/Administrator.html");
		
		int userid = Integer.parseInt(UserID);
		int active = 0; 
		
		if (Activation != null)
			active = Integer.parseInt(Activation);
        
		user = database.getById(userid);
		
        if (user == null) return Response.temporaryRedirect(location).build();
        
        if (active == 1)
        	database.activateUser(userid);	
        else
        	database.deactivateUser(userid);	
        
        return Response.temporaryRedirect(location).build();
	}
	
	@GET
	@Path("export/users")
	@Produces(MediaType.APPLICATION_XML)
	public Response exportUsers() throws JAXBException
	{
		DatabaseConnection database = new DatabaseConnection();	
		
	    JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Marshal the employees list in console
	    List<User> users = database.getUsers();
	    Users usersXML = new Users();
	    usersXML.setUsers(users);

	    ResponseBuilder response = Response.ok((Object) usersXML).header("Content-Disposition", "attachment; filename=export.xml");
		return response.build();
	}
	
	@GET
	@Path("export/listings")
	@Produces(MediaType.APPLICATION_XML)
	public Response exportListings() throws JAXBException {
		DatabaseConnection database = new DatabaseConnection();	
		
	    JAXBContext jaxbContext = JAXBContext.newInstance(Listings.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Marshal the employees list in console
	    List<Listing> listings = database.getListings();
	    Listings listingsXML = new Listings();
	    listingsXML.setListings(listings);

	    ResponseBuilder response = Response.ok((Object) listingsXML).header("Content-Disposition", "attachment; filename=export.xml");
		return response.build();
	}
	
	@GET
	@Path("export/bookings")
	@Produces(MediaType.APPLICATION_XML)
	public Response exportBookings() throws JAXBException {
		DatabaseConnection database = new DatabaseConnection();	
		
	    JAXBContext jaxbContext = JAXBContext.newInstance(Calendars.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Marshal the employees list in console
	    List<Calendar> calendars = database.getBookings();
	    Calendars calendarsXML = new Calendars();
	    calendarsXML.setCalendars(calendars);

	    ResponseBuilder response = Response.ok((Object) calendarsXML).header("Content-Disposition", "attachment; filename=export.xml");
		return response.build();
	}
	
	@GET
	@Path("export/reviews")
	@Produces(MediaType.APPLICATION_XML)
	public Response exportReviews() throws JAXBException {
		DatabaseConnection database = new DatabaseConnection();	
		
	    JAXBContext jaxbContext = JAXBContext.newInstance(Reviews.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	     
	    //Marshal the employees list in console
	    List<Review> reviews = database.getReviews();
	    Reviews reviewsXML = new Reviews();
	    reviewsXML.setReviews(reviews);

	    ResponseBuilder response = Response.ok((Object) reviewsXML).header("Content-Disposition", "attachment; filename=export.xml");
		return response.build();
	}
}