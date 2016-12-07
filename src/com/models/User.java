package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("address")
	private Address address;
	@JsonProperty("phoneNumber")
	private String phoneNumber;
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

	@JsonProperty("password")
	public String getPassword() {
	return password;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	@JsonProperty("password")
	public void setPassword(String password) {
	this.password = password;
	}

	/**
	* 
	* @return
	* The firstname
	*/
	@JsonProperty("username")
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	@JsonProperty("username")
	public void setUsername(String username) {
	this.username = username;
	}
	/**
	* 
	* @return
	* The lastname
	*/
	@JsonProperty("lastname")
	public String getLastname() {
	return lastname;
	}

	/**
	* 
	* @param lastname
	* The lastname
	*/
	@JsonProperty("lastname")
	public void setLastname(String lastname) {
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
	public String getPhoneNumber() {
	return phoneNumber;
	}

	/**
	* 
	* @param phoneNumber
	* The phoneNumber
	*/
	@JsonProperty("phoneNumber")
	public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
	}

	/**
	* 
	* @param <planWedding>
	 * @return
	* The planWedding
	*/
	
	

	}


