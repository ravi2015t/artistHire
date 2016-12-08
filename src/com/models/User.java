package com.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	
	@JsonProperty("Address")
	private Address Address;
	
	@JsonProperty("phoneNumber")
	private String phoneNumber;
		/**
	* 
	* @return
	* The firstname
	*/
	public String getFirstname() {
	return firstname;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	public void setFirstname(String firstname) {
	this.firstname = firstname;
	}

	public String getPassword() {
	return password;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	public void setPassword(String password) {
	this.password = password;
	}

	/**
	* 
	* @return
	* The firstname
	*/
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param firstname
	* The firstname
	*/
	public void setUsername(String username) {
	this.username = username;
	}
	/**
	* 
	* @return
	* The lastname
	*/
	public String getLastname() {
	return lastname;
	}

	/**
	* 
	* @param lastname
	* The lastname
	*/
	public void setLastname(String lastname) {
	this.lastname = lastname;
	}

	/**
	* 
	* @return
	* The address
	*/
	public Address getAddress() {
	return Address;
	}

	/**
	* 
	* @param address
	* The address
	*/
	public void setAddress(Address address) {
	this.Address = address;
	}

	/**
	* 
	* @return
	* The phoneNumber
	*/
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


