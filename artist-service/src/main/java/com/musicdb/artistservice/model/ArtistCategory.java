package com.musicdb.artistservice.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ArtistCategory {

	INDIVIDUAL("individual"), GROUP("group"), ORCHESTRA("orchestra"), CHOIR("choir");

	private ArtistCategory(String key) {
		this.key = key;
	}

	private String key;

	@JsonValue
	public String getKey() {
		return key;
	}
}
