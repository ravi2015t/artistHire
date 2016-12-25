package com.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorSignup {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("username")
	private String username;
	@JsonProperty("password")
	private String password;
	@JsonProperty("price")
	private Long price;
	@JsonProperty("phoneNumber")
	private String phoneNumber;
	@JsonProperty("Address")
	private Address Address;
	@JsonProperty("category")
	private String category;
	
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
	* The username
	*/
	@JsonProperty("username")
	public String getUsername() {
	return username;
	}

	/**
	* 
	* @param username
	* The username
	*/
	@JsonProperty("username")
	public void setUsername(String username) {
	this.username = username;
	}

	/**
	* 
	* @return
	* The password
	*/
	@JsonProperty("password")
	public String getPassword() {
	return password;
	}

	/**
	* 
	* @param password
	* The password
	*/
	@JsonProperty("password")
	public void setPassword(String password) {
	this.password = password;
	}

	/**
	* 
	* @return
	* The price
	*/
	@JsonProperty("price")
	public Long getPrice() {
	return price;
	}

	/**
	* 
	* @param price
	* The Price
	*/
	@JsonProperty("price")
	public void setPrice(Long price) {
	this.price = price;
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
	 * @return
	 * The phoneNumber
	 */
	@JsonProperty("category")
	public String getCategoryNumber() {
		return category;
	}

	/**
	 *
	 * @param category
	 * The category
	 */
	@JsonProperty("category")
	public void setCategoryNumber(String category) {
		this.category = category;
	}

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


	
	}

