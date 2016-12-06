package com.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmedEvent {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("venue")
	private Address venue;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("category")
	private String category;
	@JsonProperty("date")
	private Date date;
	@JsonProperty("artist")
	private String artist;
	@JsonProperty("price")
	private int price;
	
	
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
	public void setCategory(String category) {
	this.category = category;
	}

	/**
	* 
	* @return
	* The date
	*/
	@JsonProperty("date")
	public Date getDate() {
	return date;
	}

	/**
	* 
	* @param date
	* The date
	*/
	@JsonProperty("date")
	public void setDate(Date date) {
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

	/**
	* 
	* @return
	* The price
	*/
	@JsonProperty("price")
	public int getPrice() {
	return price;
	}

	/**
	* 
	* @param price
	* The price
	*/
	@JsonProperty("price")
	public void setPrice(int price) {
	this.price = price;
	}


	}


