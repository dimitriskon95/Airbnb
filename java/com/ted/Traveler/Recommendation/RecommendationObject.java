package com.ted.Traveler.Recommendation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.ted.Traveler.Database.Score;

public class RecommendationObject {
	private long objectId;	
	private ArrayList<Long> listingList;
	private ArrayList<Double> objectVector;										//Vector
	
	//before calling constructor we create a list with all reviews from the same user 
	//(put to the array list 1555 items with zero values (initialization) 
	//Create an Recommendation Object item based on the items in the list
	//set its value in the correct slot (listing_number) in the ArrayList
	public RecommendationObject(ArrayList<Score> objectscorelist, int dimensions) {			//objectlist contains all scores items for the same user
		
		if (objectscorelist.isEmpty()) this.objectId = -1;
		else this.objectId = (long) objectscorelist.get(0).getReviewerId();
		
		listingList = new ArrayList<Long>();
		objectVector = new ArrayList<Double>();
		for(int i=0; i<dimensions; i++) {
			objectVector.add(0.0);								//double 0
		}

		for(int i=0; i<objectscorelist.size(); i++) {
			objectVector.set(objectscorelist.get(i).getListingNumber(),(double) objectscorelist.get(i).getScore());
			listingList.add((long) objectscorelist.get(i).getListingId());
		}
	}

	public long getObjectId() {
		return objectId;
	}


	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}


	public ArrayList<Double> getObjectVector() {
		return objectVector;
	}


	public void setObjectVector(ArrayList<Double> objectVector) {
		this.objectVector = objectVector;
	}

	
	public ArrayList<Long> getListingList() {
		return listingList;
	}

	public void setListingList(ArrayList<Long> listingList) {
		this.listingList = listingList;
	}

	public void print(){
	//	System.out.print("Object: ");
	//	for(int i=0; i<data.size();i++)
	//		System.out.print(data.get(i).intValue());
		System.out.print(objectId+" ");
	}
	
	public void print2(){
	//	System.out.print("Object: ");
	//	for(int i=0; i<data.size();i++)
	//		System.out.print(data.get(i).intValue());
		
        try{
            PrintWriter writer = new PrintWriter("/home/Documents/items.txt", "UTF-8");
    		writer.print(objectId+" ");
            writer.close();
        } catch (IOException e) {
        	System.out.println(objectId);
        }
	}
		

/*		//Normalization
		//int sum=0;
		double average=0;
	//	for(int i=0; i<objectlist.size(); i++) {
	//		sum = sum + objectlist.get(i).getScore();
	//	}
	//	average = sum / objectlist.size();
	*/	
}