package com.ted.Traveler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.sun.org.apache.xerces.internal.util.URI;
import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.User;

@Path("profile")
public class ProfileService {
	
	@GET
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("userid") int userid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		return database.getById(userid);
	}
	
	@POST
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public void editUser(UserProfileEntity edituser, @PathParam("userid") int userid) throws Exception {
		
		DatabaseConnection database = new DatabaseConnection();
		
		if (edituser.getFirstname() != null && !(edituser.getFirstname().length() == 0))
			database.editFirstname(userid, edituser.getFirstname());
		
		if (edituser.getLastname() != null && !(edituser.getLastname().length() == 0))
			database.editLastname(userid, edituser.getLastname());
		
		if (edituser.getEmail() != null && !(edituser.getEmail().length() == 0))
			database.editEmail(userid, edituser.getEmail());
		
		if (edituser.getPhone() != null && !(edituser.getPhone().length() == 0))
			database.editPhone(userid, edituser.getPhone());

		if (edituser.getLocation() != null && !(edituser.getLocation().length() == 0))
			database.editLocation(userid, edituser.getLocation());
		
		if (edituser.getExplanation() != null && !(edituser.getExplanation().length() == 0))
			database.editExplanation(userid, edituser.getExplanation());
		
		if (edituser.getPassword() != null && !(edituser.getPassword().length() == 0) && edituser.getNewpassword() != null && !(edituser.getNewpassword().length() == 0))
			database.updatePassword(userid, edituser.getPassword(), edituser.getNewpassword());
		
	}
	
	
	@POST
	@Path("/{userid}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, 
						   @FormDataParam("file") FormDataContentDisposition fileDetail,
						   @PathParam("userid") String identifier) throws URISyntaxException {

		String uploadedFileLocation = "C:/Users/faih/workspace/Traveler/src/main/webapp/img" + fileDetail.getFileName();
		
		File file = new File(uploadedFileLocation);
		if (file.isDirectory()) {
			java.net.URI location = new java.net.URI("http://localhost:8080/Traveler/Profile.html");
			return Response.temporaryRedirect(location).build();
		}
		
		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		System.out.println("File uploaded to : " + uploadedFileLocation);
		
		DatabaseConnection database = new DatabaseConnection();
		database.editPicture(Integer.parseInt(identifier), "img/" + fileDetail.getFileName());

	    java.net.URI location = new java.net.URI("http://localhost:8080/Traveler/Profile.html");
		return Response.temporaryRedirect(location).build();
		
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	@GET
	@Path("/{userid}/image")
	@Produces(MediaType.APPLICATION_ATOM_XML)
	public String getProfileImage(@PathParam("userid") String identifier) throws Exception {

		String directory = "/home/dimitris/Documents/eclipse/Traveler/src/main/webapp/ProfilePhotos/"+ identifier;
		File theDir = new File(directory); 
		File[] files = theDir.listFiles(); 
		
		for (File f:files)  
		{  
		    return "ProfilePhotos/"+ identifier + "/" + f.getName();
		}
		return null;  
	}
	

}