package com.musicdb.artistservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.musicdb.artistservice.model.Artist;

public interface IArtistService {

	Artist saveArtist(Artist artist);

	Artist saveArtistWithoutRefresh(Artist artist);

	Artist updateArtist(Artist artist);

	Artist updateArtistWithoutRefresh(Artist artist);

	Page<Artist> findAll(Pageable pageable);

	Page<Artist> search(String keyword, Pageable pageable);
    
	Artist findById(String id);
}
