package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmedEvent {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("vendor")
	private VendorDetails vendor;
	@JsonProperty("venue")
	private Address venue;
	@JsonProperty("vendor")
	public VendorDetails getVendor() {
	return vendor;
	}

	/**
	* 
	* @param vendor
	* The vendor
	*/
	@JsonProperty("vendor")
	public void setVendor(VendorDetails vendor) {
	this.vendor = vendor;
	}

	/**
	* 
	* @return
	* The venue
	*/
	@JsonProperty("venue")
	public Object getVenue() {
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

	}


