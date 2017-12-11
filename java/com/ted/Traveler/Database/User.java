package com.ted.Traveler.Database;

import java.io.Serializable;
import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userID = :id"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UserID")
	private int userID;

	@Column(name="Active")
	private byte active;

	@Column(name="Email")
	private String email;

	@Lob
	@Column(name="Explanation")
	private String explanation;

	@Column(name="FirstName")
	private String firstName;

	@Column(name="HostID")
	private String hostID;

	@Column(name="LastName")
	private String lastName;

	@Column(name="Location")
	private String location;

	@Column(name="Password")
	private String password;

	@Column(name="Phone")
	private String phone;

	@Lob
	@Column(name="Picture")
	private String picture;

	@Column(name="Role")
	private String role;

	@Column(name="Username")
	private String username;

	public User() {
	}
	
	public User(Integer id, String username, String firstName, String lastName, String password, String role, String email, String phone, String explanation, String location) {
		this.userID = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.picture = "";
		this.location = location;
		this.explanation = explanation;

		if (role.equals("Renter")) active = 1;
		else this.active = 0;
	}
	
	public User(Integer id, String username, String firstName, String lastName, String password, String role, String email, String phone, String explanation, String picture, String location, String hostid) {
		this.userID = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.picture = picture;
		this.explanation = explanation;
		this.location = location;
		this.active = 1;
		this.hostID = hostid;
	}


	public int getUserID() {
		return this.userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExplanation() {
		return this.explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getHostID() {
		return this.hostID;
	}

	public void setHostID(String hostID) {
		this.hostID = hostID;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}