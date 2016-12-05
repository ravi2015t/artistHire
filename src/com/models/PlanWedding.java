package com.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanWedding {

	@JsonInclude(JsonInclude.Include.NON_NULL)

	@JsonProperty("date")
	private Date date;
	@JsonProperty("address")
	private Address address;
	@JsonProperty("budget")
	private Long budget;
	@JsonProperty("preferenceOrder")
	private String preferenceOrder;
	
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
	* The budget
	*/
	@JsonProperty("budget")
	public Long getBudget() {
	return budget;
	}

	/**
	* 
	* @param budget
	* The budget
	*/
	@JsonProperty("budget")
	public void setBudget(Long budget) {
	this.budget = budget;
	}

	/**
	* 
	* @return
	* The preferenceOrder
	*/
	@JsonProperty("preferenceOrder")
	public String getPreferenceOrder() {
	return preferenceOrder;
	}

	/**
	* 
	* @param preferenceOrder
	* The preferenceOrder
	*/
	@JsonProperty("preferenceOrder")
	public void setPreferenceOrder(String preferenceOrder) {
	this.preferenceOrder = preferenceOrder;
	}


	}

