package com.musicdb.artistservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.musicdb.artistservice.model.Artist;

public interface IArtistRepository {

	Artist saveArtist(Artist artist, Boolean refresh);

	Artist updateArtist(Artist artist, Boolean refresh);

	Page<Artist> findAll(Pageable pageable);

	Page<Artist> search(String keyword, Pageable pageable);

	Artist findById(String id);
   
	Page<Artist> searchAndFilter(String parentId,String name,Pageable pageable);
}
