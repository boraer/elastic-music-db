package com.musicdb.albumservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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

import com.musicdb.albumservice.AlbumServiceApplication;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.Artist;
import com.musicdb.albumservice.model.Comment;
import com.musicdb.albumservice.model.Genre;
import com.musicdb.albumservice.model.Rating;
import com.musicdb.albumservice.repository.impl.AlbumRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AlbumServiceApplication.class)
public class AlbumRepositoryIntegrationTest {

	private static String id;

	@Autowired
	private AlbumRepository albumRepository;

	@Test
	public void createAlbumTest() {

		Album album = createAlbum();
		Album created = albumRepository.saveAlbum(album, true);
		id = created.getId();
		album = createAlbum();
		albumRepository.saveAlbum(album, true);
	}

	@Test
	void updateAlbum() {

		Album album = albumRepository.findById(id);
		album.setAlbumName("Bounce");
		album.setReleaseYear(2002);
		album.setGenre("Pop");
		albumRepository.updateAlbum(album, true);

	}
	
	@Test
	void seachAndFindAlbum() {
		Pageable pageable = PageRequest.of(0, 20, Sort.by(Order.desc("albumName")));
		Page albumNamePage = albumRepository.searchByAlbumName("Bounce", pageable);
		Page albumFindByArtist = albumRepository.findByArtistId("WWKbu3EBvvcDw7wj7WMP", pageable);
		List<Album> albumFindByArtistFilterGenre = albumRepository.searchArtistAlbumsByGenre("WWKbu3EBvvcDw7wj7WMP", "Pop");
		
		albumFindByArtist.forEach(System.out::println);
		albumFindByArtistFilterGenre.forEach(System.out::println);
		
		assertThat(albumFindByArtist.getSize()).isNotEqualTo(0);
		assertThat(albumFindByArtistFilterGenre.size()).isNotEqualTo(0);
		assertThat(albumNamePage.getSize()).isNotEqualTo(0);
		
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
