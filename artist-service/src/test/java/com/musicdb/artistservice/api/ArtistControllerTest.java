package com.musicdb.artistservice.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicdb.artistservice.controller.impl.ArtistController;
import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.model.ArtistCategory;
import com.musicdb.artistservice.service.impl.ArtistService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = { ArtistController.class })
public class ArtistControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ArtistService artistService;

	@Captor
	ArgumentCaptor<Pageable> pageCaptor;
	
	@Captor
	ArgumentCaptor<String> keywordCaptor;
	
	
	final String id = UUID.randomUUID().toString();
	final String parentId = UUID.randomUUID().toString();

	@Test
	public void testSaveArtistCreated() throws Exception {

		Artist artist = createArtist(null);
		when(artistService.saveArtist(artist)).thenReturn(artist);
		mvc.perform(post("/artists").content(objectMapper.writeValueAsString(artist))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

	}

	@Test
	public void testSaveArtistBadRequest() throws Exception {

		Artist artist = createArtist(null);
		artist.setArtistCategory(null);
		artist.setName(null);
		when(artistService.saveArtist(artist)).thenReturn(artist);
		mvc.perform(post("/artists").content(objectMapper.writeValueAsString(artist))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());

	}
	
	@Test
	public void testupdateArtist() throws Exception {
		
		Artist artist = createArtist(null);
		when(artistService.saveArtist(artist)).thenReturn(artist);
		mvc.perform(put("/artists/"+artist.getId()).content(objectMapper.writeValueAsString(artist))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	
	@Test
	public void testSearchArtist() throws Exception {
		
		mvc.perform(get("/artists").param("page","0").param("size","5").param("sort","name,asc").param("keyword", "Nir")).andExpect(status().isOk());
		verify(artistService,times(1)).search(keywordCaptor.capture(), pageCaptor.capture());
		PageRequest pageable = (PageRequest) pageCaptor.getValue();
		assertThat(pageable.getPageNumber()).isEqualTo(0);
	    assertThat(pageable.getPageSize()).isEqualTo(5);
	    assertThat(pageable.getSort()).isEqualTo(Sort.by(Order.asc("name")));
	    
	}
	
	
	private Artist createArtist(String parentId) {
		return new Artist(id, "AC/DC", "AC/DC are an Australian rock band formed in Sydney in 1973 ", ArtistCategory.GROUP, "1973-01-01", "Australia",
				parentId);
	}
	
	

}
