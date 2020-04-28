package com.musicdb.albumservice.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.musicdb.albumservice.exception.DocumentNotFoundException;
import com.musicdb.albumservice.exception.DocumentOperationException;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.TrackList;
import com.musicdb.albumservice.service.IAlbumDetailService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlbumDetailService implements IAlbumDetailService {

	@Value("${discogs.url}")
	private String discogsUrl;

	@Value("${discogs.token}")
	private String discogsToken;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	ObjectMapper objectMapper;

	@HystrixCommand(fallbackMethod = "getAlbumTrackList_FallBack")
	@Override
	public List<TrackList> getAlbumTrackList(String resourceUrl) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);

		if (response.getStatusCodeValue() != 200) {
			log.error("Get Resource Url Error HttpStatus({}), Response({})", response.getStatusCodeValue(),
					response.getBody());
			return Collections.emptyList();
		}

		final JsonNode responseNode = createNode(response.getBody());

		Collection<JsonNode> trackNodes = getCollection(TrackList.INDEX_NAME, responseNode);
		List<TrackList> tracList = new LinkedList<TrackList>();

		for (final JsonNode trackNode : trackNodes) {
			final TrackList track = objectMapper.treeToValue(trackNode, TrackList.class);
			tracList.add(track);
		}
		return tracList;
	}

	@HystrixCommand(fallbackMethod = "getAlbumDetailUrl_FallBack")
	@Override
	public String getAlbumDetailUrl(final String artistId, final Album album) throws Exception {
		return getAlbumDetailUrl(artistId, album.getAlbumName(), getArtistName(artistId, album));
	}

	@Override
	public String getAlbumDetailUrl(String artistId, String albumName, String artistName) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(discogsUrl).queryParam("type", "master")
				.queryParam("release_title", albumName.toLowerCase()).queryParam("artist",artistName.toLowerCase())
				.queryParam("token", discogsToken).queryParam("page", 0).queryParam("per_page", 1);

		HttpEntity<?> entity = new HttpEntity<>(headers);
		String finalUrl = builder.toUriString();
		finalUrl = decodeURLParam(finalUrl);
		ResponseEntity<String> response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity,
				String.class);

		final JsonNode responseNode = createNode(response.getBody());
		final Integer itemCount = getFieldAsInt("items", responseNode);
		if (itemCount == null || itemCount == 0)
			throw new DocumentNotFoundException("Album can not be fetch");
		 Collection<JsonNode> results = getCollection("results", responseNode);
		 
		return getFieldAsText("resource_url", results.iterator().next());
	}

	public String getAlbumDetailUrl_FallBack(final String artistId, final Album album) {
		log.error("CIRCUIT BREAKER ENABLED!!! No Response From Search Album Service at this moment.");
		return null;
	}

	public List<TrackList> getAlbumTrackList_FallBack(String resourceUrl) {
		log.error("CIRCUIT BREAKER ENABLED!!! No Response From Master Release Service");
		return Collections.emptyList();
	}

	private static String getArtistName(final String artistId, final Album album) {
		return album.getArtists().stream().filter(a -> a.getArtistId().equals(artistId)).findFirst().get().getName();

	}

	private JsonNode createNode(final String response) throws Exception {
		return objectMapper.readTree(response);
	}
    
	private static String decodeURLParam(final String decodedString) throws UnsupportedEncodingException {
	        return URLDecoder.decode(decodedString, StandardCharsets.UTF_8.name());
	    }
	
	private String getFieldAsText(final String fieldName, final JsonNode node) {
		  final JsonNode value = node.findValue(fieldName);
	        if (value == null || value.isNull()) {
	            return  null;
	        }

	        if (!value.isTextual()) {
	            throw new DocumentOperationException("%s must be string value", fieldName);
	        }
	        return value.asText();
	}

	private Integer getFieldAsInt(final String fieldName, final JsonNode node) {
		   final JsonNode value = node.findValue(fieldName);
	        if (value == null || value.isNull()) {
	            return  null;
	        }

	        if (!value.isInt()) {
	            throw new DocumentOperationException("%s must be boolean value", fieldName);
	        }
	        return value.asInt();
	}

	public static Collection<JsonNode> getCollection(String fieldName, JsonNode jsonNode) {
		JsonNode tempNode = jsonNode.get(fieldName);
		if (tempNode == null || tempNode.isNull()) {
			return null;
		}

		if (tempNode.isArray()) {
			final ArrayNode node = (ArrayNode) jsonNode.get(fieldName);

			if (node.isArray()) {
				return new ArrayList<JsonNode>() {
					{
						Iterator<JsonNode> iterator = node.iterator();
						while (iterator.hasNext()) {
							add(iterator.next());
						}
					}
				};
			}
			throw new DocumentOperationException("%s must be collection");
		} else {
			return new ArrayList<JsonNode>() {
				{
					add(tempNode);
				}
			};
		}
	}

}
