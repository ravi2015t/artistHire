package com.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("address")
	private Address address;
	@JsonProperty("date")
	private Date date;
	@JsonProperty("price")
	private Long price;
	
	/**
	* 
	* @return
	* The address
	*/
	@JsonProperty("address")
	public Object getAddress() {
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
	* The date
	*/
	@JsonProperty("date")
	public Object getDate() {
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
	* The price
	*/
	@JsonProperty("price")
	public Object getPrice() {
	return price;
	}

	/**
	* 
	* @param price
	* The price
	*/
	@JsonProperty("price")
	public void setPrice(Long price) {
	this.price = price;
	}

	
	
}
