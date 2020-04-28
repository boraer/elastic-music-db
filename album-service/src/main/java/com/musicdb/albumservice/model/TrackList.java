package com.musicdb.albumservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrackList {

	public static final String INDEX_NAME = "tracklist";
	
	String duration;
	String position;
	String type_;
	String title;
}
