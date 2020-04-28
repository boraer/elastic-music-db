package com.musicdb.albumservice.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.musicdb.albumservice.AlbumServiceApplication;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.TrackList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AlbumServiceApplication.class)
public class AlbumDetailServiceIntegrationTest {

	@Autowired
	private IAlbumDetailService albumDetailService;
	
	@Autowired
	private IAlbumService albumService;
	
	@Test
	public void testDetail() throws Exception {
	
		final String url = albumDetailService.getAlbumDetailUrl("WWKbu3EBvvcDw7wj7WMP","Bounce","Bon Jovi");
		List<TrackList> trackList = albumDetailService.getAlbumTrackList(url);
		trackList.forEach(System.out::println);
	}
	
}
