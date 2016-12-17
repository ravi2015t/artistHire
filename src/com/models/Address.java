package com.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBDocument
public class Address {

	
	@JsonProperty("street")
	private String street;
	@JsonProperty("city")
	private String city;
	@JsonProperty("state")
	private String state;
	@JsonProperty("zip")
	private int zip;

	@DynamoDBAttribute(attributeName = "street")
	public String getStreet() {
		return street;
	}

	/**
	 * 
	 * @param srteet
	 *            The srteet
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * 
	 * @return The city
	 */
	
	@DynamoDBAttribute(attributeName = "city")
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 *            The city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * @return The state
	 */
	@DynamoDBAttribute(attributeName = "state")
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 *            The state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return The zip
	 */
	@DynamoDBAttribute(attributeName = "zip")
	public int getZip() {
		return zip;
	}

	/**
	 * 
	 * @param zip
	 *            The zip
	 */
	public void setZip(int zip) {
		this.zip = zip;
	}

}
