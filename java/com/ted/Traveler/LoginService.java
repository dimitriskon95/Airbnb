package com.ted.Traveler;

//import java.util.Date;
//import java.security.Key;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.User;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;


@Path("login")
public class LoginService {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User loginuser) throws Exception {
		DatabaseConnection database = new DatabaseConnection();
		
		String username = loginuser.getUsername();
		String password = loginuser.getPassword();
		
		if(username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) 
		{
	        return Response.status(Response.Status.NOT_FOUND).entity("Missing parameters").type(MediaType.TEXT_PLAIN).build();
	    }
		
	    User user = database.loginService(username, password);
	    
	    if(user == null) 
	    {
	        return Response.status(Response.Status.NOT_FOUND).entity("Invalid username").type(MediaType.TEXT_PLAIN).build();
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	    String json = ow.writeValueAsString(user);
	    
	    return Response.ok(json, MediaType.TEXT_PLAIN).build();
	}
	/*
	private String issueToken(String username) {
		Key key = com.ted.Traveler.utilities.KeyHolder.key;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		long expMillis = nowMillis + 300000L;
        Date exp = new Date(expMillis);
		String jws = Jwts.builder()
				  .setSubject(username)
				  .setIssuedAt(now)
				  .signWith(SignatureAlgorithm.HS512, key)
				  .setExpiration(exp)
				  .compact();
		return jws;
    }
	*/
}