package com.musicdb.albumservice.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.musicdb.albumservice.model.Album;

public interface IAlbumRepository {

	Album saveAlbum(Album album, Boolean refresh);

	Album updateAlbum(Album album, Boolean refresh);

	Page<Album> findAll(Pageable pageable);

	Album findById(String id);

	Page<Album> findByArtistId(String id, Pageable pageable);

	Page<Album> searchByAlbumName(String name, Pageable pageable);

	List<Album> searchArtistAlbumsByGenre(String id, String genre);

}
