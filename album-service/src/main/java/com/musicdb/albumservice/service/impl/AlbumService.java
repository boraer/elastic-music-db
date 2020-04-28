package com.musicdb.albumservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.repository.IAlbumRepository;
import com.musicdb.albumservice.service.IAlbumDetailService;
import com.musicdb.albumservice.service.IAlbumService;
import com.musicdb.albumservice.service.action.Actions;

@Qualifier("albumService")
@Service
public class AlbumService implements IAlbumService {

	private IAlbumRepository albumRepository;
	
	@Autowired
	public AlbumService(IAlbumRepository albumRepository) {
		this.albumRepository = albumRepository;
	}

	@Override
	public Album saveAlbum(Album album) {
		return albumRepository.saveAlbum(album, true);
	}

	@Override
	public Album saveAlbumWithoutRefresh(Album album) {
		return albumRepository.saveAlbum(album, false);
	}

	@Override
	public Album updateAlbum(Album album) {
		return albumRepository.updateAlbum(album, true);
	}

	@Override
	public Album updateAlbumWithoutRefresh(Album album) {
		return albumRepository.updateAlbum(album, false);
	}

	@Override
	public Page<Album> findArtistAlbums(String artistId, Pageable pageable) {
		return albumRepository.findByArtistId(artistId, pageable);
	}

	@Override
	public Page<Album> searchArtistAlbumsByGenre(String artistId, String genre, Pageable pageable) {
		List<Album> albums = albumRepository.searchArtistAlbumsByGenre(artistId, genre);
		Page<Album> page = new PageImpl<Album>(albums, pageable, albums.size());
		return page;
	}

	@Override
	public Album updateAlbum(String albumId, Actions actions) {

		return null;
	}

}
