package com.musicdb.artistservice.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.musicdb.artistservice.ArtistServiceApplication;
import com.musicdb.artistservice.exception.DocumentOperationException;
import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.model.ArtistCategory;
import com.musicdb.artistservice.service.IArtistService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ArtistServiceApplication.class)
public class ArtistRepositoryIntegrationTest {

	@Autowired
	private IArtistRepository artistRepository;

	@Test
	public void createArtistTest() throws Exception {
		Artist artist = new Artist(null, "AC/DC", "AC/DC are an Australian rock band formed in Sydney in 1973 ", ArtistCategory.GROUP, "1973-01-01", "Australia",
				null);
		final Artist acdc = artistRepository.saveArtist(artist, true);
		artist = new Artist(null, "Malcom Young", "Born in Australia", ArtistCategory.INDIVIDUAL, "1988-07-18", "Australia", acdc.getId());
		artistRepository.saveArtist(artist, true);
		
		artist = new Artist(null, "Niran Uysal", "Born in Ankara", ArtistCategory.INDIVIDUAL, "1980-06-15", "Turkey",
				null);
		artistRepository.saveArtist(artist, true);
		artist = new Artist(null, "Jennifer Lopez ", "Born in ABD", ArtistCategory.INDIVIDUAL, "1969-07-24", "ABD", null);
		artistRepository.saveArtist(artist, false);
	}

	@Test
	public void getAllArtistTest() throws Exception {

		Pageable pageable = PageRequest.of(0, 5, Sort.by(Order.desc("name")));
		Page page = artistRepository.search("Nir", pageable);
		assertTrue(!page.isEmpty());
	}

	@Test
	public void updateArtist() {
		Pageable pageable = PageRequest.of(0, 1, Sort.by(Order.desc("name")));
		Page page = artistRepository.search("Niran", pageable);
		Artist artist = (Artist) page.getContent().get(0);
		artist.setName("Edip Akbayram");
		final Artist updatedArtist = artistRepository.updateArtist(artist, true);
		assertTrue(updatedArtist.getName().equals(artist.getName()));

	}

	@Test
	public void createArtistTest_ParentNot_Exist() {
		Artist artist = new Artist(null, "Ercan Oya Bora", "Born in Ankara", ArtistCategory.GROUP, "1988-07-18",
				"Turkey", "asdasewllvldksa");
		Artist createdArtist = artistRepository.saveArtist(artist, true);
		assertThat(createdArtist.getId()).isNotNull();
	}
	
	@Test
	public void macthAndFilterQueryForArtist() {
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Order.desc("name")));
		Page page = artistRepository.searchAndFilter("26k1uXEBLiRuIWU1AK-e", "E", pageable);
		page.forEach(System.out::println);
	}
	
}
