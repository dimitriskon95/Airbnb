package com.ted.Traveler;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.User;

@Path("register")
public class RegistrationService {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(UserRegistration user) throws Exception {
		
		DatabaseConnection database = new DatabaseConnection();
		int userid = database.generateUserID();
	    
		if (database.usernameExists(user.getUsername()))
		{
	        return Response.status(Response.Status.NOT_FOUND).entity("Username: " + user.getUsername() + " in use").type(MediaType.TEXT_PLAIN).build();
		}
				
		User newuser = new User(userid, user.getUsername(), user.getFirstname(), user.getLastname(), user.getPassword(), user.getRole(), user.getEmail(), user.getPhone(), user.getExplanation(), user.getLocation());
		database.insertUser(newuser);

		return Response.ok().build();
	}
}