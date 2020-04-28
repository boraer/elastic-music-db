package com.musicdb.albumservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.Artist;
import com.musicdb.albumservice.model.Rating;
import com.musicdb.albumservice.repository.impl.AlbumRepository;
import com.musicdb.albumservice.service.action.Action;
import com.musicdb.albumservice.service.action.ActionType;
import com.musicdb.albumservice.service.action.ActionValidatorService;
import com.musicdb.albumservice.service.action.Actions;
import com.musicdb.albumservice.service.impl.AlbumService;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Mock
	AlbumRepository albumRepository;
	
	@InjectMocks
	AlbumService albumService;
	
	@Captor
	ArgumentCaptor<Album> albumCaptor;
	
	@Captor
	ArgumentCaptor<Boolean> refreshCaptor;
	

	@Test
	public void createAlbumTest() {
		Album album = createAlbum();
		albumService.saveAlbum(album);
		verify(albumRepository, times(1)).saveAlbum(albumCaptor.capture(), refreshCaptor.capture());
		album = albumCaptor.getValue();
		Boolean refresh = refreshCaptor.getValue();
		assertTrue(refresh);
		assertThat(album).isNotNull();
	}
	
	@Test
	public void updateAlbumTest() {
		Pageable pageable = PageRequest.of(0, 20, Sort.by(Order.desc("albumName")));
		Album album = createAlbum();
		
		Page<Album> page = new PageImpl<Album>(Arrays.asList(album));
		lenient().when(albumRepository.findByArtistId(album.getArtists().iterator().next().getArtistId(), pageable)).thenReturn(page);
		albumService.saveAlbum(album);
		verify(albumRepository, times(1)).saveAlbum(albumCaptor.capture(), refreshCaptor.capture());
		album = albumCaptor.getValue();
		Boolean refresh = refreshCaptor.getValue();
		assertTrue(refresh);
		assertThat(album).isNotNull();
	}
	
	@Test void actionArtistTest() throws Exception {
		
		Artist artist = new Artist("WWKbu3EBvvcDw7wj7WMP","Bon Jovi New");
		
		ActionValidatorService actionValidatorService = new ActionValidatorService(objectMapper);
		Action action = new Action();
	    action.setAction(ActionType.ADD_ARTIST_ACTION.getKey());
	    action.setField(ActionType.ADD_ARTIST_ACTION.getField());
	    action.setObject(objectMapper.writeValueAsString(artist));
		List<Action> actions = Collections.singletonList(action);
		String actionReq = objectMapper.writeValueAsString(actions);
		
		//actionValidatorService.buildUpdateObject(createAlbum(), actionReq);
	}
	
	private Album createAlbum() {

		Artist artist = new Artist("WWKbu3EBvvcDw7wj7WMP", "Bon Jovi");
		Album album = new Album();

		album.setAlbumName("Slippery When Wet");
		album.setReleaseYear(1986);
		album.setArtists(Collections.singleton((artist)));
		album.setGenre("Hard Rock, Pop");
		album.setRating(new Rating(0, 0));
		return album;
	}
	
}
