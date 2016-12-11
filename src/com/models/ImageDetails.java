package com.models;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageDetails {
	@JsonProperty("path")
	private String path;
	@JsonProperty("name")
	private String name;
	/**
	* 
	* @param srteet
	* The srteet
	*/
	@JsonProperty("path")
	public void setPath(String path) {
	this.path = path;
	}
	@JsonProperty("path")
	public String getPath() {
	return path;
	}

	/**
	* 
	* @return
	* The city
	*/
	@JsonProperty("name")
	public String getName() {
	return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
	this.name = name;
	}
	
}
