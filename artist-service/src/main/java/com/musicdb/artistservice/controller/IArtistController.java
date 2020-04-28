package com.musicdb.artistservice.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.musicdb.artistservice.model.Artist;

@RequestMapping(path = "/artists")
public interface IArtistController {

	@PostMapping
	ResponseEntity<Artist> createArtist(@Valid@RequestBody final Artist artist);

	@PutMapping(path = "/{artistId}")
	Artist updateArtist(@PathVariable("artistId") String artistId, @RequestBody final Artist artist);

	@GetMapping
	Page<Artist> getArtists(@RequestParam(required = false, name = "keyword") final String keyword,
			@PageableDefault(page = 0, size = 20) @SortDefault.SortDefaults({
					@SortDefault(sort = "name", direction = Direction.DESC) }) Pageable pageable);

}
