package com.musicdb.artistservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.repository.IArtistRepository;
import com.musicdb.artistservice.service.IArtistService;

@Service
public class ArtistService implements IArtistService {

	private IArtistRepository artistRepository;

	@Autowired
	public ArtistService(IArtistRepository artistRepository) {
		this.artistRepository = artistRepository;
	}

	@Override
	public Artist saveArtist(Artist artist) {
		parentCheck(artist);
		return artistRepository.saveArtist(artist, true);
	}

	@Override
	public Artist saveArtistWithoutRefresh(Artist artist) {
		parentCheck(artist);
		return artistRepository.saveArtist(artist, false);
	}

	@Override
	public Artist updateArtist(Artist artist) {
		parentCheck(artist);
		return artistRepository.updateArtist(artist, true);
	}

	@Override
	public Artist updateArtistWithoutRefresh(Artist artist) {
		parentCheck(artist);
		return artistRepository.updateArtist(artist, false);
	}

	@Override
	public Page<Artist> findAll(Pageable pageable) {
		return artistRepository.findAll(pageable);
	}

	@Override
	public Page<Artist> search(String keyword, Pageable pageable) {
		return artistRepository.search(keyword, pageable);
	}
	
	private void parentCheck(Artist artist) {
		if (artist.getParentId() != null)
			artistRepository.findById(artist.getParentId());
	}

}
