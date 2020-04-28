package com.musicdb.albumservice.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.AlbumDetailed;
import com.musicdb.albumservice.model.TrackList;
import com.musicdb.albumservice.service.IAlbumDetailService;
import com.musicdb.albumservice.service.IAlbumService;
import com.musicdb.albumservice.service.action.Actions;

@Service
@Qualifier("detailedAlbumService")
public class AlbumServiceDecorator {

	private IAlbumService albumService;

	private IAlbumDetailService albumDetailService;

	@Autowired
	public AlbumServiceDecorator(@Qualifier("albumService") IAlbumService albumService,
			IAlbumDetailService albumDetailService) {
		this.albumService = albumService;
		this.albumDetailService = albumDetailService;
	}

	public Album saveAlbum(Album album) {
		return albumService.saveAlbum(album);
	}

	public Album saveAlbumWithoutRefresh(Album album) {
		return albumService.saveAlbumWithoutRefresh(album);
	}

	public Album updateAlbum(Album album) {
		return albumService.updateAlbum(album);
	}

	public Album updateAlbumWithoutRefresh(Album album) {
		return albumService.saveAlbumWithoutRefresh(album);
	}

	public Page<AlbumDetailed> findArtistAlbums(String artistId, Pageable pageable) {
		Page page = albumService.findArtistAlbums(artistId, pageable);
		return generateDetails(artistId, page, pageable);
	}

	public Album updateAlbum(String albumId, Actions actions) {
		return albumService.updateAlbum(albumId, actions);
	}

	public Page<AlbumDetailed> searchArtistAlbumsByGenre(String artistId, String genre, Pageable pageable) {
		Page page = albumService.searchArtistAlbumsByGenre(artistId, genre, pageable);
		return generateDetails(artistId, page, pageable);
	}

	private Page<AlbumDetailed> generateDetails(String artistId, Page page, Pageable pageable) {
		List<Album> albumList = page.getContent();
		List<AlbumDetailed> albumListDetailed = new LinkedList<AlbumDetailed>();
		for (final Album album : albumList) {
			List<TrackList> trackList = new LinkedList<TrackList>();
			String url;
			try {
				url = albumDetailService.getAlbumDetailUrl(artistId, album);
				if (url != null)
					trackList = albumDetailService.getAlbumTrackList(url);
			} catch (Exception e) {
				e.getMessage();
			}
			albumListDetailed.add(new AlbumDetailed(album.getId(), album.getAlbumName(), album.getReleaseYear(),
					album.getArtists(), album.getGenre(), album.getRating(), trackList));

		}

		Page<AlbumDetailed> pageDetailed = new PageImpl<AlbumDetailed>(albumListDetailed, pageable,
				albumListDetailed.size());

		return pageDetailed;

	}
}
