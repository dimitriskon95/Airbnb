package com.ted.Traveler.Recommendation;

import java.util.ArrayList;

public class RecommendationHeuristic {
	private int dimensions;
	private ArrayList<Double> heuristicArray;
	
	
	public RecommendationHeuristic(int dims) {
		dimensions = dims;
		this.heuristicArray = new ArrayList<Double>();
		for (int i = 0; i < dimensions; i++)
			heuristicArray.add(uniform_distribution_Marsaglia(-1, 1));
	}


	public int getDimensions() {
		return dimensions;
	}


	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}


	public ArrayList<Double> getHeuristicArray() {
		return heuristicArray;
	}


	public void setHeuristicArray(ArrayList<Double> heuristicArray) {
		this.heuristicArray = heuristicArray;
	}


	//Pseudorandom generator marsaglia
	public double uniform_distribution_Marsaglia(int min, int max) {
		double y1, y2, r, tempvalue;
		do{
	    	y1 = Math.random() * (max - min) + min;
	   		y2 = Math.random() * (max - min) + min;
	   		r = Math.pow(y1,2)+ Math.pow(y2,2);
	   	}while(r < 1);
	   	
		tempvalue = Math.abs(((-2)*Math.log(r))/(r));
	   	return (Math.sqrt(tempvalue)*y1);
	}
}