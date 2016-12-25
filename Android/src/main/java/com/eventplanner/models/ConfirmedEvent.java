package com.eventplanner.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmedEvent {
	@JsonInclude(JsonInclude.Include.NON_NULL)

	@JsonProperty("venue")
	private Address venue;


	@JsonProperty("usr")
	private String usr;

	@JsonProperty("category")
	private String category;
	@JsonProperty("date")
	private String date;
	@JsonProperty("artist")
	private String artist;


	/**
	 *
	 * @return
	 * The venue
	 */
	@JsonProperty("venue")
	public Address getVenue() {
		return venue;
	}

	/**
	 *
	 * @param venue
	 * The venue
	 */
	@JsonProperty("venue")
	public void setVenue(Address venue) {
		this.venue = venue;
	}

	@JsonProperty("category")
	public String getCategory() {
		return this.getCategory();
	}

	@JsonProperty("category")
	public void setCategory(String category) {
		this.category = category;
	}


	@JsonProperty("usr")
	public String getUsr() {
		return usr;
	}

	/**
	 *
	 * @param username
	 * The username
	 */
	@JsonProperty("usr")
	public void setUsr(String usr) {
		this.usr = usr;
	}


	/**
	 *
	 * @return
	 * The date
	 */
	@JsonProperty("date")
	public String getDate() {
		return this.date;
	}


	/**
	 *
	 * @param date
	 * The date
	 */
	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 *
	 * @return
	 * The artist
	 */
	@JsonProperty("artist")
	public String getArtist() {
		return artist;
	}

	/**
	 *
	 * @param artist
	 * The artist
	 */
	@JsonProperty("artist")
	public void setArtist(String artist) {
		this.artist = artist;
	}


}



