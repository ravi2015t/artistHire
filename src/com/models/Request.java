package com.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("address")
	private Address address;
	@JsonProperty("user")
	private String user;
	@JsonProperty("message")
	private String message;
	@JsonProperty("date")
	private Date date;
	
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

	/**
	* 
	* @return
	* The user
	*/
	@JsonProperty("user")
	public String getUser() {
	return user;
	}

	/**
	* 
	* @param user
	* The user
	*/
	@JsonProperty("user")
	public void setUser(String user) {
	this.user = user;
	}

	/**
	* 
	* @return
	* The message
	*/
	@JsonProperty("message")
	public String getMessage() {
	return message;
	}

	/**
	* 
	* @param message
	* The message
	*/
	@JsonProperty("message")
	public void setMessage(String message) {
	this.message = message;
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

	
	
}
