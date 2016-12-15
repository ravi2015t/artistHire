package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketPrice {
	@JsonProperty("photographer")
	private Long photographer;
	@JsonProperty("florist")
	private Long florist;
	@JsonProperty("makeupartist")
	private Long makeupartist;
	
	public Long getPhotographer() {
		return photographer;
	}
	public void setPhotographer(Long photographer) {
		this.photographer = photographer;
	}
	public Long getFlorist() {
		return florist;
	}
	public void setFlorist(Long florist) {
		this.florist = florist;
	}
	public Long getMakeupArtist() {
		return makeupartist;
	}
	public void setMakeupArtist(Long makeupArtist) {
		this.makeupartist = makeupArtist;
	}
	
	
}
