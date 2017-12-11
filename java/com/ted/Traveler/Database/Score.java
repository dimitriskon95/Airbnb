package com.ted.Traveler.Database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the scores database table.
 * 
 */
@Entity
@Table(name="scores")
@NamedQuery(name="Score.findAll", query="SELECT s FROM Score s ORDER BY s.reviewerId")
public class Score implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="listing_id")
	private int listingId;

	@Column(name="listing_number")
	private int listingNumber;

	@Column(name="reviewer_id")
	private int reviewerId;

	private int score;

	public Score() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getListingId() {
		return this.listingId;
	}

	public void setListingId(int listingId) {
		this.listingId = listingId;
	}

	public int getListingNumber() {
		return this.listingNumber;
	}

	public void setListingNumber(int listingNumber) {
		this.listingNumber = listingNumber;
	}

	public int getReviewerId() {
		return this.reviewerId;
	}

	public void setReviewerId(int reviewerId) {
		this.reviewerId = reviewerId;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}