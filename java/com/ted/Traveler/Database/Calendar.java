package com.ted.Traveler.Database;

import java.io.Serializable;
import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the calendar database table.
 * 
 */
@Entity
@Table(name="calendar")
@NamedQuery(name="Calendar.findAll", query="SELECT c FROM Calendar c")
@XmlRootElement(name = "calendar")
@XmlAccessorType(XmlAccessType.FIELD)
public class Calendar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Lob
	private String available;

	@Lob
	private String date;

	@Column(name="listing_id")
	private int listingId;

	@Lob
	private String price;

	private int userid;

	public Calendar() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAvailable() {
		return this.available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getListingId() {
		return this.listingId;
	}

	public void setListingId(int listingId) {
		this.listingId = listingId;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}