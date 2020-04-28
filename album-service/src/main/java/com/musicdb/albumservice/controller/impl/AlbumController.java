package com.musicdb.albumservice.controller.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.musicdb.albumservice.controller.IAlbumController;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.AlbumDetailed;
import com.musicdb.albumservice.service.IAlbumService;
import com.musicdb.albumservice.service.impl.AlbumServiceDecorator;

@RestController
public class AlbumController implements IAlbumController {

	private AlbumServiceDecorator albumService;

	@Autowired
	public AlbumController(AlbumServiceDecorator albumService) {
		this.albumService = albumService;
	}

	@Override
	public ResponseEntity<Album> createAlbum(HttpServletRequest request, @Valid Album album) {
		return new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.CREATED);
	}

	@Override
	public Album updateAlbum(HttpServletRequest request, String albumId, Album album) {
		album.setId(albumId);
		return albumService.updateAlbum(album);
	}

	@Override
	public Page<AlbumDetailed> getAlbums(HttpServletRequest request, String artistId, String genre, Pageable pageable) {
		if (genre == null)
			return albumService.findArtistAlbums(artistId, pageable);
		else
			return albumService.searchArtistAlbumsByGenre(artistId, genre,pageable);
	}

}
