package com.ted.Traveler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.Listing;

@Path("search")
public class SearchService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public List<Listing> searchService(SearchEntity searchEntity) throws Exception {
		DatabaseConnection database = new DatabaseConnection();	
		int guests;
		double pricemin, pricemax;
		boolean privateroom, entireroom, sharedroom, heating, cooling, kitchen, parking, elevator, wifi, tv;
		
		if (searchEntity == null) 
			return database.getListings();
		
		if (searchEntity.getPrivateroom() != null) privateroom = true; else privateroom = false;
		if (searchEntity.getEntireroom() != null) entireroom  = true; else entireroom = false;
		if (searchEntity.getSharedroom() != null) sharedroom  = true; else sharedroom = false;
		if (searchEntity.getHeating() != null) heating = true; else heating = false;
		if (searchEntity.getCooling() != null) cooling = true; else cooling = false;
		if (searchEntity.getKitchen() != null) kitchen = true; else kitchen = false;
		if (searchEntity.getParking() != null) parking = true; else parking = false;
		if (searchEntity.getElevator() != null) elevator = true; else elevator = false;
		if (searchEntity.getWifi() != null) wifi = true; else wifi = false;
		if (searchEntity.getTv() != null) tv = true; else tv = false;

		if (searchEntity.getGuests() == null || searchEntity.getGuests().equals("") ) guests = 0;
		else guests = Integer.parseInt(searchEntity.getGuests());
		
		if (searchEntity.getPricemin() == null || searchEntity.getPricemin().equals("")) pricemin = 10;
		else pricemin = Double.parseDouble(searchEntity.getPricemin());
		
		if (searchEntity.getPricemax() == null || searchEntity.getPricemax().equals("")) pricemax = 1000;
		else pricemax = Double.parseDouble(searchEntity.getPricemax());
		
		if (searchEntity.getCheckin().equals("") && !searchEntity.getCheckout().equals("")) 
			searchEntity.setCheckout("");
					
		List<Listing> tmplistings = database.search(searchEntity.getLocation(), searchEntity.getCheckin(), searchEntity.getCheckout(), guests, pricemin, pricemax);
		List<Listing> listings = new ArrayList<Listing>();
		
		
		for (Iterator<Listing> iterator = tmplistings.iterator(); iterator.hasNext();) {
			Listing listing = iterator.next();

			if (!checkRoomType(privateroom, entireroom, sharedroom, listing))
				continue;
			
			if (heating && !listing.getAmenities().contains("Heating"))
				continue;
			
			if (cooling && !listing.getAmenities().contains("Air Conditioning"))
				continue;
			
			if (kitchen && !listing.getAmenities().contains("Kitchen"))
				continue;
			
			if (parking && !listing.getAmenities().contains("Free Parking on Premises"))
				continue;
			
			if (elevator && !listing.getAmenities().contains("Elevator in Building"))
				continue;
			
			if (wifi && !listing.getAmenities().contains("Wireless Internet"))
				continue;
			
			if (tv && !listing.getAmenities().contains("TV"))
				continue;
				
			listings.add(listing);
		}
		return listings;
	}
	
	public boolean checkRoomType(boolean privateroom,boolean entireroom,boolean sharedroom, Listing listing) {
		
		if (privateroom == false && entireroom == false && sharedroom == false)		//no room type given
			return true;
		
		if ((privateroom && listing.getRoomType().equals("Private room")) || (entireroom && listing.getRoomType().equals("Entire home/apt")) || (sharedroom && listing.getRoomType().equals("Shared room")))
			return true;
		
		return false;
	}
	
}