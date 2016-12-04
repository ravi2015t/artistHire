package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("srteet")
	private String srteet;
	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;
	@JsonProperty("zip")
	private int zip;
	@JsonProperty("srteet")
	public String getSrteet() {
	return srteet;
	}

	/**
	* 
	* @param srteet
	* The srteet
	*/
	@JsonProperty("srteet")
	public void setSrteet(String srteet) {
	this.srteet = srteet;
	}

	/**
	* 
	* @return
	* The city
	*/
	@JsonProperty("city")
	public String getCity() {
	return city;
	}

	/**
	* 
	* @param city
	* The city
	*/
	@JsonProperty("city")
	public void setCity(String city) {
	this.city = city;
	}

	/**
	* 
	* @return
	* The state
	*/
	@JsonProperty("state")
	public String getState() {
	return state;
	}

	/**
	* 
	* @param state
	* The state
	*/
	@JsonProperty("state")
	public void setState(String state) {
	this.state = state;
	}

	/**
	* 
	* @return
	* The zip
	*/
	@JsonProperty("zip")
	public int getZip() {
	return zip;
	}

	/**
	* 
	* @param zip
	* The zip
	*/
	@JsonProperty("zip")
	public void setZip(int zip) {
	this.zip = zip;
	}

	
	
	
}
