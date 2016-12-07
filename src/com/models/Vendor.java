package com.models;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vendor {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
	@JsonProperty("Price")
	private Long price;
	@JsonProperty("phoneNumber")
	private String phoneNumber;
	@JsonProperty("address")
	private Address address;
	@JsonProperty("Calendar")
	private Calendar calendar;
	@JsonProperty("rating")
	private int rating;
	@JsonProperty("Nusers")
	private int nusers;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("firstname")
	public String getFirstname() {
	return firstname;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	@JsonProperty("type")
	public void setType(String type) {
	this.type = type;
	}
	@JsonProperty("type")
	public String getType() {
	return type;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	@JsonProperty("firstname")
	public void setFirstname(String firstname) {
	this.firstname = firstname;
	}

	/**
	* 
	* @return
	* The lastname
	*/
	@JsonProperty("lastname")
	public String getLastname() {
	return lastname;
	}

	/**
	* 
	* @param lastname
	* The lastname
	*/
	@JsonProperty("lastname")
	public void setLastname(String lastname) {
	this.lastname = lastname;
	
	}

	/**
	* 
	* @return
	* The username
	*/
	@JsonProperty("username")
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param username
	* The username
	*/
	@JsonProperty("username")
	public void setUsername(String username) {
	this.username = username;
	}

	/**
	* 
	* @return
	* The password
	*/
	@JsonProperty("password")
	public String getPassword() {
	return password;
	}

	/**
	* 
	* @param password
	* The password
	*/
	@JsonProperty("password")
	public void setPassword(String password) {
	this.password = password;
	}

	/**
	* 
	* @return
	* The price
	*/
	@JsonProperty("Price")
	public Long getPrice() {
	return price;
	}

	/**
	* 
	* @param price
	* The Price
	*/
	@JsonProperty("Price")
	public void setPrice(Long price) {
	this.price = price;
	}

	/**
	* 
	* @return
	* The phoneNumber
	*/
	@JsonProperty("phoneNumber")
	public String getPhoneNumber() {
	return phoneNumber;
	}

	/**
	* 
	* @param phoneNumber
	* The phoneNumber
	*/
	@JsonProperty("phoneNumber")
	public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
	}

	/**
	* 
	* @return
	* The address
	*/
	@JsonProperty("address")
	public Address getAddress() {
	return address;
	}

	/**
	* 
	* @param address
	* The address
	*/
	@JsonProperty("address")
	public void setAddress(Address address) {
	this.address = address;
	}

		@JsonProperty("Calendar")
	public Object getCalendar() {
	return calendar;
	}

	/**
	* 
	* @param calendar
	* The Calendar
	*/
	@JsonProperty("Calendar")
	public void setCalendar(Calendar calendar) {
	this.calendar = calendar;
	}

	/**
	* 
	* @return
	* The rating
	*/
	@JsonProperty("rating")
	public int getRating() {
	return rating;
	}

	/**
	* 
	* @param rating
	* The rating
	*/
	@JsonProperty("rating")
	public void setRating(int rating) {
	this.rating = rating;
	}

	
	}

