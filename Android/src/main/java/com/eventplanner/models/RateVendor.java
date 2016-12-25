package com.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RateVendor {

	@JsonProperty("username")
	private String username;
	@JsonProperty("rating")
	private int rating;
	
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param srteet
	 *            The srteet
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return The city
	 */
	public int getRating() {
		return this.rating;
	}

	/**
	 * 
	 * @param city
	 *            The city
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

}
