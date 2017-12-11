package com.ted.Traveler.Recommendation;

import java.util.ArrayList;

public class RecommendationHashtable {
	private int hash_functions;
	private int dimensions;
	private int number_of_buckets;
	private ArrayList<RecommendationBucket> hashtable;
	private ArrayList<RecommendationHeuristic> gfunction;

	public RecommendationHashtable(int hash_functions, int dimensions, int number_of_bucketlists) {
		this.hash_functions = hash_functions;
		this.dimensions = dimensions;
		this.number_of_buckets = number_of_bucketlists;
		this.hashtable = new ArrayList<RecommendationBucket>();
		for(int i=0; i<number_of_bucketlists; i++)
		{
			RecommendationBucket bucket = new RecommendationBucket();
			hashtable.add(bucket);
		}
		this.gfunction = new ArrayList<RecommendationHeuristic>();
		for(int i=0; i<hash_functions; i++)
		{
			RecommendationHeuristic heuristic = new RecommendationHeuristic(dimensions);
			gfunction.add(heuristic);
		}
	}
	
	public void insert(RecommendationObject object) {
		
		int listIndex=0, exponent = hash_functions-1;
		double innerproduct;
		ArrayList<Character> cosineChars = new ArrayList<Character>();
		for(int i=0; i<hash_functions; i++) {
			innerproduct = inner_Product(object.getObjectVector(), gfunction.get(i).getHeuristicArray());
			if (innerproduct >= 0) cosineChars.add('1');
			else cosineChars.add('0');
		}
		for(int i=0; i<hash_functions; i++) {
			if (cosineChars.get(i).charValue() == '1') 
				listIndex += Math.pow(2, exponent);        							// k bits -> integer
			exponent--;
		}										
		
		hashtable.get(listIndex).getBucketlist().add(object);
	}
	
	public ArrayList<RecommendationObject> getBucketList(RecommendationObject object) {
		
		int listIndex=0, exponent = hash_functions-1;
		double innerproduct;
		ArrayList<Character> cosineChars = new ArrayList<Character>();
		for(int i=0; i<hash_functions; i++) {
			innerproduct = inner_Product(object.getObjectVector(), gfunction.get(i).getHeuristicArray());
			if (innerproduct >= 0) cosineChars.add('1');
			else cosineChars.add('0');
		}
		for(int i=0; i<hash_functions; i++) {
			if (cosineChars.get(i).charValue() == '1') 
				listIndex += Math.pow(2, exponent);        							// k bits -> integer
			exponent--;
		}			
		return hashtable.get(listIndex).getBucketlist();
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

	public int getNumber_of_bucketlists() {
		return number_of_buckets;
	}

	public void setNumber_of_bucketlists(int number_of_bucketlists) {
		this.number_of_buckets = number_of_bucketlists;
	}

	public ArrayList<RecommendationBucket> getHashtable() {
		return hashtable;
	}

	public void setHashtable(ArrayList<RecommendationBucket> hashtable) {
		this.hashtable = hashtable;
	}
	
	public ArrayList<RecommendationHeuristic> getGfunction() {
		return gfunction;
	}

	public void setGfunction(ArrayList<RecommendationHeuristic> gfunction) {
		this.gfunction = gfunction;
	}

	double inner_Product(ArrayList<Double> p, ArrayList<Double> vector)
	{
		double sum = 0;
		for(int i=0; i<= dimensions-1; i++){
			sum += p.get(i).doubleValue() * vector.get(i).doubleValue();
		}
		return sum;
	}
	
	public void print(){
		for(int i=0; i<hashtable.size(); i++){
			hashtable.get(i).printBucket();
		}
		System.out.println();
	}
}