package com.musicdb.artistservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.musicdb.artistservice.exception.DocumentNotFoundException;
import com.musicdb.artistservice.exception.DocumentOperationException;
import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.model.ArtistCategory;
import com.musicdb.artistservice.repository.impl.ArtistRepository;
import com.musicdb.artistservice.service.impl.ArtistService;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

	@Mock
	ArtistRepository artistRepository;

	@InjectMocks
	ArtistService artistService;

	@Captor
	ArgumentCaptor<Artist> artistCaptor;

	@Captor
	ArgumentCaptor<Boolean> refreshOptionCaptor;

	final String id = UUID.randomUUID().toString();
	final String parentId = UUID.randomUUID().toString();

	@Test
	public void testCreateArtist() {
		artistService.saveArtist(createArtist(null));

		verify(artistRepository, times(1)).saveArtist(artistCaptor.capture(), refreshOptionCaptor.capture());
		Artist artist = artistCaptor.getValue();
		Boolean refresh = refreshOptionCaptor.getValue();
		assertTrue(refresh);
		assertThat(artist.getId()).isNotNull();

	}

	@Test
	public void testCreateArtist_withParent() {
		
		Artist parent = createArtist(null);
		lenient().when(artistRepository.findById(parent.getId())).thenReturn(parent);
		artistService.saveArtist(createArtist(parentId));
		verify(artistRepository, times(1)).saveArtist(artistCaptor.capture(), refreshOptionCaptor.capture());
		Artist artist = artistCaptor.getValue();
		Boolean refresh = refreshOptionCaptor.getValue();
		assertTrue(refresh);
		assertThat(artist.getId()).isNotNull();

	}

	@Test
	public void testCreateArtist_withParent_ParentNotExist() {
		Artist parent = createArtist(null);
		when(artistRepository.findById(parent.getId())).thenThrow(new DocumentNotFoundException("Not Found"));
		assertThrows(DocumentNotFoundException.class, () -> artistService.saveArtist(createArtist(parent.getId())));
	}

	@Test
	public void testCreateArtist_withNoRefresh() {
		artistService.saveArtistWithoutRefresh(createArtist(null));
		verify(artistRepository, times(1)).saveArtist(artistCaptor.capture(), refreshOptionCaptor.capture());
		Artist artist = artistCaptor.getValue();
		Boolean refresh = refreshOptionCaptor.getValue();
		assertTrue(!refresh);
		assertThat(artist.getId()).isNotNull();

	}

	@Test
	public void testUpdateArtist_withRefresh() {
		artistService.updateArtist(createArtist(null));
		verify(artistRepository, times(1)).updateArtist(artistCaptor.capture(), refreshOptionCaptor.capture());
		Artist artist = artistCaptor.getValue();
		Boolean refresh = refreshOptionCaptor.getValue();
		assertTrue(refresh);
		assertThat(artist.getId()).isNotNull();

	}

	private Artist createArtist(String parentId) {
		return new Artist(id, "AC/DC", "AC/DC are an Australian rock band formed in Sydney in 1973 ", ArtistCategory.GROUP, "1973-01-01", "Australia",
				parentId);
	}
	

}
