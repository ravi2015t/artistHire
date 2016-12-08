package com.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("Address")
	private Address Address;
	@JsonProperty("user")
	private String user;
	@JsonProperty("vendor")
	private String vendor;
	
	@JsonProperty("date")
	private Date date;
	
	/**
	* 
	* @return
	* The address
	*/
	@JsonProperty("Address")
	public Address getAddress() {
	return Address;
	}

	/**
	* 
	* @param address
	* The address
	*/
	@JsonProperty("Address")
	public void setAddress(Address address) {
	this.Address = address;
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
	@JsonProperty("vendor")
	public String getVendor() {
	return this.vendor;
	}

	/**
	* 
	* @param message
	* The message
	*/
	@JsonProperty("vendor")
	public void setVendor(String vendor) {
	this.vendor = vendor;
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
