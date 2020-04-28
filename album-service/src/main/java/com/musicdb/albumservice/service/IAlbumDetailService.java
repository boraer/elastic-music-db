package com.musicdb.albumservice.service;

import java.util.List;

import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.TrackList;

public interface IAlbumDetailService {

	List<TrackList> getAlbumTrackList(String resourceUrl) throws Exception;
	
	String getAlbumDetailUrl(String artistId,Album album) throws Exception;
	
	String getAlbumDetailUrl(String artistId,String albumName,String artistName) throws Exception;
}
