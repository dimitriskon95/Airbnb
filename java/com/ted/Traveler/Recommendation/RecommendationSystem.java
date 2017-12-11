package com.ted.Traveler.Recommendation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ted.Traveler.Database.Calendar;
import com.ted.Traveler.Database.DatabaseConnection;
import com.ted.Traveler.Database.History;
import com.ted.Traveler.Database.Listing;
import com.ted.Traveler.Database.Score;

public class RecommendationSystem {

	private int hash_tables;																				//number default = 5
	private int hash_functions;																				//number default = 4
	private int dimensions;																					//number of listings
	private int pnumber;																					//number of closest neighbors be checked
	private ArrayList<RecommendationObject> objectList = new ArrayList<RecommendationObject>();				//List of all items
	private ArrayList<RecommendationHashtable> hashtableList = new ArrayList<RecommendationHashtable>(); 	//List of Hashtables
	
	public RecommendationSystem(int dims){
		hash_tables = 1;
		hash_functions = 5;
		dimensions = dims;
		pnumber = 5;
		for(int i=0; i<hash_tables; i++)
		{
			RecommendationHashtable hahstable = new RecommendationHashtable(hash_functions, dimensions, (int) Math.pow(2, hash_functions));
			hashtableList.add(hahstable);
		}
	}

	public int getHash_tables() {
		return hash_tables;
	}

	public void setHash_tables(int hash_tables) {
		this.hash_tables = hash_tables;
	}

	public int getHash_functions() {
		return hash_functions;
	}

	public void setHash_functions(int hash_functions) {
		this.hash_functions = hash_functions;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int getPnumber() {
		return pnumber;
	}

	public void setPnumber(int pnumber) {
		this.pnumber = pnumber;
	}
	
	
	public ArrayList<Listing> RecommendationMenu(int userid, boolean isReviewer)				//userStatus = true if has made a review else false
	{
		DatabaseConnection database = new DatabaseConnection();
		List<Score> scores = database.getScores();
		
		if (isReviewer) System.out.println("Recommendation Menu 1");
		else System.out.println("Recommendation Menu 2");
		
		//Create object(user-vector) from Score Table
		for(int i=0; i<scores.size()/5; i++) {
			Score listingscore = scores.get(i);
			int reviewerid = listingscore.getReviewerId();
			ArrayList<Score> scorelist = new ArrayList<Score>();
			scorelist.add(listingscore);
			for(int j=i+1; j<scores.size(); j++) {
				listingscore = scores.get(j);
				if (reviewerid == listingscore.getReviewerId()) {
					scorelist.add(listingscore);
					i++;
				}
				else {
					break;
				}
			}
			RecommendationObject recommendationobject = new RecommendationObject(scorelist, getDimensions());
			objectList.add(recommendationobject);
		}
		
		//insert every item from the List to Hash tables
		for(Iterator<RecommendationObject> iterator = objectList.iterator(); iterator.hasNext();){
			RecommendationObject object = iterator.next();
			for(int i=0; i<hashtableList.size(); i++)
				hashtableList.get(i).insert(object);
			iterator.remove();
		}
		
		//Clear the list
		this.objectList.clear();

		RecommendationObject user_recommendationobject = null;
		if (isReviewer) user_recommendationobject = new RecommendationObject((ArrayList<Score>) database.getUserScores(userid), getDimensions());	//user-reviewer		
		else user_recommendationobject = getUserRecommendationObject(userid);																		//Bonus Implementation 

		List<RecommendationObject> neighborslist = new ArrayList<RecommendationObject>();															//Neighbors object from same buckets
		
		for(int i=0; i<hashtableList.size(); i++)
		{
			ArrayList<RecommendationObject> bucketlist = hashtableList.get(i).getBucketList(user_recommendationobject);
			for(Iterator<RecommendationObject> iterator = bucketlist.iterator(); iterator.hasNext();){
				RecommendationObject object = iterator.next();
				boolean exists = false;
				for(int k=0; k<neighborslist.size(); k++){
					if (object.getObjectId() == neighborslist.get(k).getObjectId()) {
						exists = true;
						break;
					}
				}
				if (!exists) neighborslist.add(object);
			}
		}

		if (pnumber > neighborslist.size()) pnumber = neighborslist.size();													//If list has less items than pnumber value(=5)
				
		ArrayList<Listing> recommendlistings = new ArrayList<Listing>();
		while(recommendlistings.size() < pnumber && !neighborslist.isEmpty()) {
			RecommendationObject closestNeighbor = closestNeighbor(user_recommendationobject, neighborslist);
			for(int i=0; i<closestNeighbor.getListingList().size(); i++) {
				if (!listingExists(recommendlistings, closestNeighbor.getListingList().get(i).longValue())) {
					recommendlistings.add(database.getListing((int) closestNeighbor.getListingList().get(i).longValue()));
				}
			}
		}

		if (isReviewer) System.out.println("Recommendation Menu 1 finished");
		else System.out.println("Recommendation Menu 2 finished");
		
		return recommendlistings;
	}

	//Check if a listing is already one of the recommended 
	public boolean listingExists(ArrayList<Listing> recommendlistings, long listingid) 
	{
		for(Iterator<Listing> iterator = recommendlistings.iterator(); iterator.hasNext();) {
			Listing listing = iterator.next();
			if (listing.getId() == listingid)
				return true;
		}
		return false;
	}
	
	//Find the temporary closest neighbor
	public RecommendationObject closestNeighbor(RecommendationObject recommendationobject, List<RecommendationObject> neighborslist)
	{
		double distance=0, min = 10000, sum=0;
		int index=0, closestIndex=0;
		RecommendationObject neighbor;
		
		if (neighborslist.isEmpty()) return null;
		
		for(Iterator<RecommendationObject> iterator = neighborslist.iterator(); iterator.hasNext();){
			RecommendationObject object = iterator.next();
			if (object.getObjectId() != recommendationobject.getObjectId())				//Not add the same object in the list
			{
				distance = 0; sum = 0;
				for(int i=0; i < recommendationobject.getObjectVector().size(); i++)
				{
					sum += Math.pow(recommendationobject.getObjectVector().get(i) - object.getObjectVector().get(i), 2);
				}
				distance = Math.sqrt(sum);
				if (distance < min) 		//to avoid itself use (... && dist > 0)
				{
					min = distance;
					closestIndex = index; 
				}
			}
			index++;
		}
		
		neighbor = neighborslist.get(closestIndex);
		neighborslist.remove(closestIndex);
		return neighbor;
	}
	
	//Create the vector of an unsigned user based on the average score of every building that they have searched
	public RecommendationObject getUserRecommendationObject(int userid) {
		DatabaseConnection database = new DatabaseConnection();
		ArrayList<Score> userScoreList = new ArrayList<Score>();
		
		List<History> historyEntries = database.getUserHistory(userid);
		for(Iterator<History> iterator = historyEntries.iterator(); iterator.hasNext();){
			History history = iterator.next();
			List<Score> scorelist = database.getScoreListings(history.getListingId());			//From Score table
			
			int averagescore=0;
			for(int i=0; i<scorelist.size(); i++){
				averagescore += scorelist.get(i).getScore();
			}
			if (scorelist.size() > 0) averagescore = averagescore / scorelist.size();
			
			Score newscore = new Score();
			newscore.setId(-1);
			newscore.setReviewerId(history.getUserId());
			newscore.setListingId(history.getListingId());
			newscore.setListingNumber(history.getListingNumber());
			newscore.setScore(averagescore);
			
			userScoreList.add(newscore);
		}
		
		/*
		//User has a history entry of the entity that has booked 
		//Not neededs
		  
		List<Calendar> booklistings = database.getBookListings(userid);
		for(Iterator<Calendar> iterator = booklistings.iterator(); iterator.hasNext();){
			Calendar calendar = iterator.next();
			List<Score> scorelist = database.getScoreListings(calendar.getListingId());			//From Score table
			
			int averagescore=0;
			for(int i=0; i<scorelist.size(); i++){
				averagescore += scorelist.get(i).getScore();
			}
			if (scorelist.size() > 0) averagescore = averagescore / scorelist.size();
			
			Score newscore = new Score();
			newscore.setId(-1);
			newscore.setReviewerId(calendar.getUserid());
			newscore.setListingId(calendar.getListingId());
			newscore.setListingNumber(database.getListingNumber(calendar.getListingId()));
			newscore.setScore(averagescore);
			
			userScoreList.add(newscore);
		}
		*/

		RecommendationObject object = new RecommendationObject(userScoreList, database.getListingsNumber());
		return object;
	}
	
	public void printHashtables() {		
		for(int i=0; i<hash_tables; i++)
		{
			System.out.println("Hashtable "+i+":");
			hashtableList.get(i).print();
		}
	}
	
}