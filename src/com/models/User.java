package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private Integer lastname;
	@JsonProperty("address")
	private Address address;
	@JsonProperty("phoneNumber")
	private Long phoneNumber;
	@JsonProperty("planWedding")
	private PlanWedding planWedding;
	@JsonProperty("confirmedEvent")
	private ConfirmedEvent confirmedEvent;
		/**
	* 
	* @return
	* The firstname
	*/
	@JsonProperty("firstname")
	public String getFirstname() {
	return firstname;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	@JsonProperty("firstname")
	public void setFirstname(String firstname) {
	this.firstname = firstname;
	}

	/**
	* 
	* @return
	* The lastname
	*/
	@JsonProperty("lastname")
	public Integer getLastname() {
	return lastname;
	}

	/**
	* 
	* @param lastname
	* The lastname
	*/
	@JsonProperty("lastname")
	public void setLastname(Integer lastname) {
	this.lastname = lastname;
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
	* The phoneNumber
	*/
	@JsonProperty("phoneNumber")
	public Long getPhoneNumber() {
	return phoneNumber;
	}

	/**
	* 
	* @param phoneNumber
	* The phoneNumber
	*/
	@JsonProperty("phoneNumber")
	public void setPhoneNumber(Long phoneNumber) {
	this.phoneNumber = phoneNumber;
	}

	/**
	* 
	* @param <planWedding>
	 * @return
	* The planWedding
	*/
	@JsonProperty("planWedding")
	public PlanWedding getPlanWedding() {
	return planWedding;
	}

	/**
	* 
	* @param planWedding
	* The planWedding
	*/
	@JsonProperty("planWedding")
	public void setPlanWedding(PlanWedding planWedding) {
	this.planWedding = planWedding;
	}

	/**
	* 
	* @return
	* The confirmedEvent
	*/
	@JsonProperty("confirmedEvent")
	public ConfirmedEvent getConfirmedEvent() {
	return confirmedEvent;
	}

	/**
	* 
	* @param confirmedEvent
	* The confirmedEvent
	*/
	@JsonProperty("confirmedEvent")
	public void setConfirmedEvent(ConfirmedEvent confirmedEvent) {
	this.confirmedEvent = confirmedEvent;
	}


	}


