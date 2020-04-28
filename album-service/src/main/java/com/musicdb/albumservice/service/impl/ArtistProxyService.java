package com.musicdb.albumservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicdb.albumservice.service.IArtistProxyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArtistProxyService implements IArtistProxyService {

	private RestTemplate restTemplate;

	@Autowired
	public ArtistProxyService(RestTemplate restTemplate, ObjectMapper objectMapper) {

		this.restTemplate = restTemplate;
	}

	@Override
	public boolean checkArtistById(String artistId) {
		ResponseEntity<String> response = restTemplate.getForEntity("http://album-service/artists/" + artistId,
				String.class);
		if (response.getStatusCodeValue() != 200) {
			log.error("Given Artist({}) is not exist", artistId);
			return false;
		}
		return true;
	}

}
