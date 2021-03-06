package com.ted.Traveler.XmlEntities;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ted.Traveler.Database.Review;

@XmlRootElement(name = "reviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reviews {

    @XmlElement(name = "review")
    private List<Review> reviews = null;

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
    
}