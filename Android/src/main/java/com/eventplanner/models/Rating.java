package com.eventplanner.models;

import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating implements Comparator<Rating> {
	@JsonProperty("rating")
	private Double rating;
	@JsonProperty("price")
	private Long price;
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("username")
	private String username;
	
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

	
	public double getRating() {
		return this.rating;
	}

	/**
	 * 
	 * @param srteet
	 *            The srteet
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public Long getPrice() {
		return price;
	}

	/**
	 * 
	 * @param srteet
	 *            The srteet
	 */
	public void setPrice(Long price) {
		this.price = price;
	}

	@Override
	public int compare(Rating cr1,Rating cr2) {
		
		 return  Double.compare(cr1.getRating(), cr2.getRating());
	}
}
