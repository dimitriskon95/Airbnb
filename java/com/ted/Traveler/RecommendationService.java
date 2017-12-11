package com.ted.Traveler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.Listing;
import com.ted.Traveler.Database.Score;
import com.ted.Traveler.Recommendation.RecommendationObject;
import com.ted.Traveler.Recommendation.RecommendationSystem;

@Path("recommendation")
public class RecommendationService {
	
	@GET
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Listing> recommendationService(@PathParam("userid") int userid) throws FileNotFoundException, IOException {
		DatabaseConnection database = new DatabaseConnection();
		RecommendationSystem recommendationsystem = new RecommendationSystem(database.getListingsNumber());
		if (database.userhasBooked(userid))
			return recommendationsystem.RecommendationMenu(userid, true);
		else
			return recommendationsystem.RecommendationMenu(userid, false);
    }

}