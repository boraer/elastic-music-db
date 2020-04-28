package com.musicdb.albumservice.model;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class AlbumDetailed extends Album {

	List<TrackList> trackList;

	public AlbumDetailed(String id, String albumName, Integer releaseYear, Set<Artist> artists, String genre,
			Rating rating, List<TrackList> trackList) {
		super(id, albumName, releaseYear, artists, genre, rating);
		this.trackList = trackList;
	}

}
