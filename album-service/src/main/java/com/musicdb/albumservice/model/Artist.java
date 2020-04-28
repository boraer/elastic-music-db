package com.musicdb.albumservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

	public static final String PATH ="artists";
	
	String artistId;
	String name;
	
}
