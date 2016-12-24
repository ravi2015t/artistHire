package com.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "vendor")
public class RecommendationResults {
	private Double rating;
	private Long price;
	private String firstname;
	private String lastname;
	private String username;
	private String category;

	@DynamoDBAttribute(attributeName = "firstname")
	public String getFirstname() {

		return this.firstname;
	}

	@DynamoDBAttribute(attributeName = "category")
	public void setCategory(String category) {
		this.category = category;
	}

	@DynamoDBAttribute(attributeName = "category")
	public String getCategory() {
		return category;
	}

	@DynamoDBAttribute(attributeName = "firstname")
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@DynamoDBAttribute(attributeName = "lastname")
	public String getLastname() {
		return lastname;
	}

	@DynamoDBAttribute(attributeName = "lastname")
	public void setLastname(String lastname) {
		this.lastname = lastname;

	}

	@DynamoDBHashKey(attributeName = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@DynamoDBAttribute(attributeName = "rating")

	public double getRating() {
		return this.rating;
	}

	@DynamoDBAttribute(attributeName = "rating")
	public void setRating(double rating) {
		this.rating = rating;
	}

	@DynamoDBAttribute(attributeName = "price")
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
