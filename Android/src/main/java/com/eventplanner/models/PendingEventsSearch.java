package com.eventplanner.models;


public class PendingEventsSearch {
	private Address Address;
	private String usr;
	private String vendor;
	private String username;
	private String date;

	

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address address) {
		Address = address;
	}


	public String getUser() {
		return usr;
	}

	public void setUser(String usr) {
		this.usr = usr;
	}


	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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
