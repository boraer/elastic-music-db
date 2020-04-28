package com.musicdb.artistservice.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.musicdb.artistservice.controller.IArtistController;
import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.service.IArtistService;

@RestController
public class ArtistController implements IArtistController {

	private IArtistService artistService;

	@Autowired
	public ArtistController(IArtistService artistService) {
		this.artistService = artistService;
	}

	@Override
	public ResponseEntity<Artist> createArtist(Artist artist) {
		 return new ResponseEntity<>(artistService.saveArtist(artist), HttpStatus.CREATED);
	}

	@Override
	public Artist updateArtist(String artistId, Artist artist) {
		artist.setId(artistId);
		return artistService.updateArtist(artist);
	}

	@Override
	public Page<Artist> getArtists(String keyword, Pageable pageable) {
		if (keyword == null)
			return artistService.findAll(pageable);
		else
			return artistService.search(keyword, pageable);
	}

}
