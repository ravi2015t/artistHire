package com.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "approveEvents")
public class PendingEventsSearch {
	private Address Address;
	private String usr;
	private String vendor;
	private String username;
	private String date;

	

	@DynamoDBAttribute(attributeName = "Address")
	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address address) {
		Address = address;
	}


	@DynamoDBAttribute(attributeName = "usr")
	public String getUser() {
		return usr;
	}

	public void setUser(String usr) {
		this.usr = usr;
	}


	@DynamoDBAttribute(attributeName = "vendor")
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@DynamoDBAttribute(attributeName = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@DynamoDBAttribute(attributeName = "date")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
     public String toString() {
         return "vendor" + this.getVendor() + "user" + this.getUser();            
     }

}
