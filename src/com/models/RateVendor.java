package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateVendor {

	@JsonProperty("username")
	private String username;

	@JsonProperty("rating")
	private double rating;

	@JsonProperty("username")
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return The city
	 */
	@JsonProperty("rating")
	public double getRating() {
		return this.rating;
	}

	/**
	 * 
	 * @param city
	 *            The city
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

}
