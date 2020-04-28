package com.musicdb.albumservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.AlbumDetailed;

@RequestMapping(path = "/artists/{artistId}/albums")
public interface IAlbumController {
	@PostMapping
	ResponseEntity<Album> createAlbum(HttpServletRequest request,@Valid @RequestBody final Album album);

	@PutMapping(path = "/{albumId}")
	Album updateAlbum(HttpServletRequest request, @PathVariable("albumId") String albumId,
			@RequestBody final Album album);

	@GetMapping
	Page<AlbumDetailed> getAlbums(HttpServletRequest request,@PathVariable("artistId") String artistId,
			@RequestParam(required = false, name = "genre") final String genre,
			@PageableDefault(page = 0, size = 20) @SortDefault.SortDefaults({
					@SortDefault(sort = "albumName", direction = Direction.DESC) }) Pageable pageable);

}
