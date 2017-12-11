package com.ted.Traveler.Database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the history database table.
 * 
 */
@Entity
@NamedQuery(name="History.findAll", query="SELECT h FROM History h")
public class History implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="listing_id")
	private int listingId;

	@Column(name="listing_number")
	private int listingNumber;

	@Column(name="user_id")
	private int userId;

	public History() {
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

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}