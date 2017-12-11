package com.ted.Traveler;

import com.ted.Traveler.Database.Message;

public class MessageEntity extends Message{

	private static final long serialVersionUID = 1L;

	String name;
	String picture_url;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture_url() {
		return picture_url;
	}
	public void setPicture_url(String picture_url) {
		this.picture_url = picture_url;
	}
	
	public MessageEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MessageEntity(int messageId, int listingId, String messageText, int receiverId, int senderId, String name,
			String picture_url) {
		super(messageId, listingId, messageText, receiverId, senderId);
		this.name = name;
		this.picture_url = picture_url;
	}
	
}