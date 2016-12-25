package com.eventplanner.models;

public class RecommendationResults {
	private Double rating;
	private Long price;
	private String firstname;
	private String lastname;
	private String username;
	private String category;

	public String getFirstname() {

		return this.firstname;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	 @Override
     public String toString() {
         return "Vendor [Type=" + category + ", name=" + firstname + ", lstname=" + lastname
         + ", price category=" + price + ", username=" + username
         + ", rating=" + rating + "]";            
     }

}
