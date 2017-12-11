package com.ted.Traveler.Database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the messages database table.
 * 
 */
@Entity
@Table(name="messages")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="message_id")
	private int messageId;

	@Column(name="listing_id")
	private int listingId;

	@Column(name="message_text")
	private String messageText;

	@Column(name="receiver_id")
	private int receiverId;

	@Column(name="sender_id")
	private int senderId;

	public Message() {
	}

	public Message(int messageId, int listingId, String messageText, int receiverId, int senderId) {
		super();
		this.messageId = messageId;
		this.listingId = listingId;
		this.messageText = messageText;
		this.receiverId = receiverId;
		this.senderId = senderId;
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getListingId() {
		return this.listingId;
	}

	public void setListingId(int listingId) {
		this.listingId = listingId;
	}

	public String getMessageText() {
		return this.messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

}