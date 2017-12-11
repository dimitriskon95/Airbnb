package com.ted.Traveler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ted.Traveler.Database.User;
import com.ted.Traveler.Database.Calendar;
import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.Listing;
import com.ted.Traveler.Database.Message;
import com.ted.Traveler.Recommendation.RecommendationSystem;

@Path("room")
public class RoomService {

	@GET
	@Path("/{roomid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Listing getListing(@PathParam("roomid") int roomid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		Listing listing = database.getListing(roomid);
		return listing;
	}

	@POST
	@Path("/{roomid}/{userid}")
	public void updateHistory(@PathParam("roomid") int roomid, @PathParam("userid") int userid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		if (!database.historyExists(userid, roomid))
			database.updateUserHistory(userid, roomid);
		RecommendationSystem rsystem = new RecommendationSystem(database.getListingsNumber());
		rsystem.getUserRecommendationObject(userid);
	}
	
	@POST
	@Path("/{roomid}/book/{userid}")
	public void bookRoom(BookingEntity bookparams) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		

		List<Calendar> calendarlist = database.getCalendarByListing(Integer.parseInt(bookparams.getRoomid()));
		
		if (calendarlist.isEmpty()) return;
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
		Date checkinDate = (Date) formatter.parse(bookparams.getCheckin());
		Date checkoutDate = (Date) formatter.parse(bookparams.getCheckout());
        long diff = checkoutDate.getTime() - checkinDate.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        //Check if dates are available
        int checkinIndex = 0;
        for(int i=0; i<calendarlist.size(); i++) {
        	Calendar calendar = calendarlist.get(i);
        	if (calendar.getDate().equals(bookparams.getCheckin())) {
        		checkinIndex = i;
        		for(int j=i; j<=checkinIndex+days; j++) {
        			if (calendarlist.get(j).getAvailable().equals("f")) {
        	    		System.out.println(calendarlist.size());
        				return;
        			}
        		}
        		break;
        	}
        }

        for(int i=checkinIndex; i<=checkinIndex+days; i++) {
        	Calendar calendar = calendarlist.get(i);
        	database.bookCalendar(calendar.getId(), Integer.parseInt(bookparams.getUserid()));
    		System.out.println(calendar.getId());
        }

		
		//List<Calendar> calendarlist = database.getCalendarByListing(roomid);*/
	}
	
	@GET
	@Path("/message/{roomid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MessageEntity> getMessage(@PathParam("roomid") int roomid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();
		Listing listing = database.getListing(roomid);
		List<Message> messages = database.getMessages(roomid, listing.getHostId());
		List<MessageEntity> messageEntities = new ArrayList<MessageEntity>();
		for (Iterator<Message> iterator = messages.iterator(); iterator.hasNext();) {
			Message message = iterator.next();
			User user = database.getById(message.getSenderId());
			MessageEntity messageEntity = new MessageEntity(message.getMessageId(), message.getListingId(), message.getMessageText(), message.getReceiverId(), message.getSenderId(), user.getFirstName(), user.getPicture());
			messageEntities.add(messageEntity);
		}
		return messageEntities;
	}
	
	@POST
	@Path("/message/post")
	@Produces(MediaType.APPLICATION_JSON)
	public void postMessageAnswer(Message postmessage) throws Exception {
		DatabaseConnection database = new DatabaseConnection();	
		//swap sender_id with host_id
		Message message = new Message(database.generateMessageID(), postmessage.getListingId(), postmessage.getMessageText(), postmessage.getSenderId(),  postmessage.getReceiverId());
		database.postMessage(message);
	}
	
	@DELETE
	@Path("/message/delete/{messageid}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteMessage(@PathParam("messageid") int messageid) throws Exception {
		System.out.println(messageid);
		DatabaseConnection database = new DatabaseConnection();		
		database.deleteMessage(messageid);
	}

	
	@POST
	@Path("/upload/{roomid}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream, 
						   @FormDataParam("file") FormDataContentDisposition fileDetail,
						   @PathParam("roomid") String roomid) throws URISyntaxException {
		
		String uploadedFileLocation = "C:/Users/faih/workspace/Traveler/src/main/webapp/img" + fileDetail.getFileName();

		File file = new File(uploadedFileLocation);
		if (file.isDirectory()) {
			java.net.URI location = new java.net.URI("http://localhost:8080/Traveler/RoomEditor.html");
			return Response.temporaryRedirect(location).build();
		}
		
		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		System.out.println("File uploaded to : " + uploadedFileLocation);
		
		DatabaseConnection database = new DatabaseConnection();
		database.editListingPicture(Integer.parseInt(roomid), "img/" + fileDetail.getFileName());

	    java.net.URI location = new java.net.URI("http://localhost:8080/Traveler/RoomEditor.html");
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
	@Path("/getListings/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Listing> getHostListings(@PathParam("userid") int userid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		User user = database.getUserById(userid);
		if (user.getHostID() == null || user.getHostID().isEmpty()) return null;
		return database.getHostListings(Integer.parseInt(user.getHostID()));
	}
	
	@POST
	@Path("/create/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Listing createListing(RoomEntity roomEntity, @PathParam("userid") int userid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		User user = database.getUserById(userid);
		Listing listing = new Listing();
		
		listing.setId(database.generateListingID());
		listing.setHostId(Integer.parseInt(user.getHostID()));
		listing.setAmenities("{}");
		
		if (roomEntity.getName() != null && !(roomEntity.getName().isEmpty())) listing.setName(roomEntity.getName());
		
		if (roomEntity.getPrice() != null && !(roomEntity.getPrice().isEmpty())) listing.setPrice(Double.parseDouble(roomEntity.getPrice()));
		
		if (roomEntity.getExtraprice() != null && !(roomEntity.getExtraprice().isEmpty())) listing.setExtraPeople(roomEntity.getExtraprice());
		
		if (roomEntity.getRoomType() != null && !(roomEntity.getRoomType().isEmpty())) listing.setRoomType(roomEntity.getRoomType());
		
		if (roomEntity.getCity() != null && !(roomEntity.getCity().isEmpty())) listing.setCity(roomEntity.getCity());
		
		if (roomEntity.getBeds() != null && !(roomEntity.getBeds().isEmpty())) listing.setBeds(Integer.parseInt(roomEntity.getBeds()));
		
		if (roomEntity.getBathrooms() != null && !(roomEntity.getBathrooms().isEmpty())) listing.setBathrooms(Double.parseDouble(roomEntity.getBathrooms()));
		
		if (roomEntity.getBedrooms() != null && !(roomEntity.getBedrooms().isEmpty())) listing.setBedrooms(Integer.parseInt(roomEntity.getBedrooms()));
		
		if (roomEntity.getSquarefeet() != null && !(roomEntity.getSquarefeet().isEmpty())) listing.setSquareFeet(roomEntity.getSquarefeet());
		
		if (roomEntity.getDescription() != null && !(roomEntity.getDescription().isEmpty())) listing.setDescription(roomEntity.getDescription());
		
		if (roomEntity.getMinimumnights() != null && !(roomEntity.getMinimumnights().isEmpty())) listing.setMinimumNights(Integer.parseInt(roomEntity.getMinimumnights()));
		
		if (roomEntity.getCancellationPolicy() != null && !(roomEntity.getCancellationPolicy().isEmpty())) listing.setCancellationPolicy(roomEntity.getCancellationPolicy());
		
		if (roomEntity.getRequireslicense() != null && !(roomEntity.getRequireslicense().isEmpty())) listing.setRequiresLicense(roomEntity.getRequireslicense());
		
		database.insertListing(listing);
		
		List<LocalDate> dates = this.updateCalendar(roomEntity.getStartDate(), roomEntity.getEndDate());
		for(Iterator<LocalDate> iterator = dates.iterator(); iterator.hasNext();) {
			LocalDate localdate = iterator.next();
			Calendar calendar = new Calendar();
			calendar.setId(database.generateCalendarID());
			calendar.setListingId(listing.getId());
			calendar.setUserid(0);
			calendar.setPrice(roomEntity.getPrice());
			calendar.setAvailable("t");
			calendar.setDate(localdate.toString());
			database.insertCalendar(calendar);
		}
		
		return listing;
	}
	
	
	public List<LocalDate> updateCalendar(String startdate, String enddate) {
		LocalDate startDate = LocalDate.parse(startdate);
		LocalDate endDate = LocalDate.parse(enddate);
		
		int days = Days.daysBetween(startDate, endDate).getDays();
		List<LocalDate> dates = new ArrayList<LocalDate>();  // Set initial capacity to `days`.
		for (int i=0; i < days; i++) {
		    LocalDate d = startDate.withFieldAdded(DurationFieldType.days(), i);
		    dates.add(d);
		}
		return dates;
	}
	

	@POST
	@Path("/edit/{roomid}")
	@Produces(MediaType.APPLICATION_JSON)
	public void editListing(RoomEntity roomEntity, @PathParam("roomid") int roomid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		Listing listing = database.getListing(roomid);
		
		if (roomEntity.getName() != null) database.editListingName(roomid, roomEntity.getName());
		
		if (roomEntity.getPrice() != null) database.editListingPrice(roomid, Double.parseDouble(roomEntity.getPrice()));
		
		if (roomEntity.getExtraprice() != null) database.editListingExtraPrice(roomid, roomEntity.getExtraprice());
		
		if (roomEntity.getRoomType() != null) database.editListingRoomType(roomid, roomEntity.getRoomType());
		
		if (roomEntity.getCity() != null) database.editListingCity(roomid, roomEntity.getCity());
		
		if (roomEntity.getBeds() != null) database.editListingBeds(roomid, Integer.parseInt(roomEntity.getBeds()));
		
		if (roomEntity.getBathrooms() != null) database.editListingBathrooms(roomid, Double.parseDouble(roomEntity.getBathrooms()));
		
		if (roomEntity.getBedrooms() != null) database.editListingBedrooms(roomid, Integer.parseInt(roomEntity.getBedrooms()));
		
		if (roomEntity.getSquarefeet() != null) database.editListingSquareFeet(roomid, roomEntity.getSquarefeet());
		
		if (roomEntity.getDescription() != null) database.editListingDescription(roomid, roomEntity.getDescription());
		
		if (roomEntity.getMinimumnights() != null) database.editListingMinimumNights(roomid, Integer.parseInt(roomEntity.getMinimumnights()));
		
		if (roomEntity.getCancellationPolicy() != null) database.editListingCancellationPolicy(roomid, roomEntity.getCancellationPolicy());
		
		if (roomEntity.getRequireslicense() != null) database.editListingRequiresLicense(roomid, roomEntity.getRequireslicense());
	}
	
	@POST
	@Path("/edit/amenity/{roomid}")
	@Produces(MediaType.APPLICATION_JSON)
	public void editListingAmenities(RoomAmenity roomAmenity, @PathParam("roomid") int roomid) throws Exception {
		DatabaseConnection database = new DatabaseConnection();		
		Listing listing = database.getListing(roomid);
		String listingAmenity = listing.getAmenities();

		String amenity = listingAmenity.substring(0, listingAmenity.length()-1);
		
		if (roomAmenity.getAirconditioning() != null && !listingAmenity.contains("Air Conditioning")) amenity = amenity + ",\"Air Conditioning\"";		
		if (roomAmenity.getAirconditioning() == null && listingAmenity.contains("Air Conditioning")) amenity = amenity.replaceAll("\"Air Conditioning\"", "");
		
		if (roomAmenity.getBreakfast() != null && !listingAmenity.contains("Breakfast")) amenity = amenity + ",Breakfast";
		if (roomAmenity.getBreakfast() == null && listingAmenity.contains("Breakfast")) amenity = amenity.replaceAll("Breakfast", "");
		
		if (roomAmenity.getElevator() != null && !listingAmenity.contains("Elevator in Building")) amenity = amenity + ",\"Elevator in Building\"";		
		if (roomAmenity.getElevator() == null && listingAmenity.contains("Elevator in Building")) amenity = amenity.replaceAll("\"Elevator in Building\"", "");
	
		if (roomAmenity.getFriendly() != null && !listingAmenity.contains("Family/Kid Friendly")) amenity = amenity + ",\"Family/Kid Friendly\"";
		if (roomAmenity.getFriendly() == null && listingAmenity.contains("Family/Kid Friendly")) amenity = amenity.replaceAll("\"Family/Kid Friendly\"", "");
		
		
		if (roomAmenity.getHeating() != null && !listingAmenity.contains("Heating")) amenity = amenity + ",Heating";
		if (roomAmenity.getHeating() == null && listingAmenity.contains("Heating")) amenity = amenity.replaceAll("Heating", "");

		if (listingAmenity.contains("\"Wireless Internet\"")) amenity = amenity.replaceAll("\"Wireless Internet\"", "");
		if (roomAmenity.getInternet() != null && !listingAmenity.contains("Internet")) amenity = amenity + ",Internet";
		if (roomAmenity.getInternet() == null && listingAmenity.contains("Internet")) amenity = amenity.replaceAll("Internet", "");
		if (listingAmenity.contains("\"Wireless Internet\"")) amenity = amenity + ",\"Wireless Internet\"";
		
		if (roomAmenity.getKitchen() != null  && !listingAmenity.contains("Kitchen")) amenity = amenity + ",Kitchen";
		if (roomAmenity.getKitchen() == null  && listingAmenity.contains("Kitchen")) amenity = amenity.replaceAll("Kitchen", "");

		if (roomAmenity.getSmoking() != null && !listingAmenity.contains("Smoking Allowed")) amenity = amenity + ",\"Smoking Allowed\"";
		if (roomAmenity.getSmoking() == null && listingAmenity.contains("Smoking Allowed")) amenity = amenity.replaceAll( "\"Smoking Allowed\"", "");

		if (listingAmenity.contains("\"Cable TV\"")) amenity = amenity.replaceAll("\"Cable TV\"", "");
		if (roomAmenity.getTv() != null && !listingAmenity.contains("TV")) amenity = amenity + ",TV";
		if (roomAmenity.getTv() == null && listingAmenity.contains("TV")) amenity = amenity.replaceAll("TV", "");
		if (listingAmenity.contains("\"Cable TV\"")) amenity = amenity + ",\"Cable TV\"";
		
		amenity = amenity + "}";
		
		String final_amenity = "{";
		for(int i=1;i<amenity.length();i++) {
			if (amenity.charAt(i) == ',' && amenity.charAt(i-1) == '{') continue;
			if (amenity.charAt(i) == ',' && amenity.charAt(i-1) == ',') continue;
			if (amenity.charAt(i) == ',' && amenity.charAt(i-1) == '}') continue;
			final_amenity = final_amenity + amenity.charAt(i);
		}
		
		database.editListingAmenities(roomid, final_amenity);
	}
	
}