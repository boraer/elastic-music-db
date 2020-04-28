package com.musicdb.albumservice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.AlbumDetailed;
import com.musicdb.albumservice.service.action.Actions;



public interface IAlbumService {
	
	Album saveAlbum(Album album);

	Album saveAlbumWithoutRefresh(Album album);

	Album updateAlbum(Album album);

	Album updateAlbumWithoutRefresh(Album album);

	Page<Album> findArtistAlbums(String artistId,Pageable pageable);

    Album updateAlbum(String albumId,Actions actions);

	Page<Album> searchArtistAlbumsByGenre(String artistId, String genre, Pageable pageable);
	
	void mappingconfig();
	
}
