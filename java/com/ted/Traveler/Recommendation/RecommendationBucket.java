package com.ted.Traveler.Recommendation;

import java.util.ArrayList;

public class RecommendationBucket {

	private ArrayList<RecommendationObject> bucketlist;

	public RecommendationBucket() {
		this.bucketlist = new ArrayList<RecommendationObject>();
	}

	public ArrayList<RecommendationObject> getBucketlist() {
		return bucketlist;
	}

	public void setBucketlist(ArrayList<RecommendationObject> bucketlist) {
		this.bucketlist = bucketlist;
	}

	public void printBucket(){
		System.out.println("Bucket:");
		for(int i=0; i<bucketlist.size(); i++) {
			bucketlist.get(i).print();
		}
		System.out.println();
	}
}