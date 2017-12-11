package com.ted.Traveler.XmlEntities;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ted.Traveler.Database.Listing;
 
@XmlRootElement(name = "listings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Listings {

    @XmlElement(name = "listing")
    private List<Listing> listings = null;

	public List<Listing> getListings() {
		return listings;
	}

	public void setListings(List<Listing> listings) {
		this.listings = listings;
	}
    
}
