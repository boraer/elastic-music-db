package com.musicdb.albumservice.repository.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicdb.albumservice.exception.DocumentNotFoundException;
import com.musicdb.albumservice.exception.DocumentOperationException;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.Artist;
import com.musicdb.albumservice.repository.IAlbumRepository;

@Repository
public class AlbumRepository implements IAlbumRepository {

	private ObjectMapper objectMapper;

	private ElasticsearchOperations elasticOperations;

	@Autowired
	public AlbumRepository(ObjectMapper objectMapper, ElasticsearchOperations elasticOperations) {
		this.objectMapper = objectMapper;
		this.elasticOperations = elasticOperations;
	}

	@Override
	public Album saveAlbum(Album album, Boolean refresh) {
		try {
			final IndexQuery indexQuery = new IndexQueryBuilder().withIndexName(Album.INDEX_NAME)
					.withType(Album.TYPE_NAME).withSource(objectMapper.writeValueAsString(album)).build();
			final String id = elasticOperations.index(indexQuery);
			if (refresh)
				elasticOperations.refresh(Album.INDEX_NAME);

			album.setId(id);
			return album;
		} catch (Exception e) {
			throw new DocumentOperationException("Add operation has an exception: " + e);
		}
	}

	@Override
	public Album updateAlbum(Album album, Boolean refresh) {
		String entity;
		try {
			entity = objectMapper.writeValueAsString(album);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new DocumentOperationException("Object parse error");
		}

		UpdateRequest updateRequest = new UpdateRequest(Album.INDEX_NAME, Album.TYPE_NAME, album.getId());
		updateRequest.doc(entity, XContentType.JSON);
		updateRequest.fetchSource(true);
		UpdateQuery updateQuery = new UpdateQueryBuilder().withIndexName(Album.INDEX_NAME).withType(Album.TYPE_NAME)
				.withId(album.getId()).withUpdateRequest(updateRequest).build();
		UpdateResponse response = elasticOperations.update(updateQuery);

		if (refresh)
			elasticOperations.refresh(Album.INDEX_NAME);

		switch (response.getResult()) {
		case UPDATED:
			return album;
		case NOT_FOUND:
			throw new DocumentNotFoundException("Document(Album) with id '%s' can not be found for Update",
					album.getId());
		default:
			throw new DocumentOperationException("Wrong Operation has been executed for Document(Album) with id '%s'",
					album.getId());

		}
	}

	@Override
	public Page<Album> findAll(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery())
				.withPageable(pageable).build();

		return elasticOperations.queryForPage(searchQuery, Album.class);
	}

	@Override
	public Album findById(String id) {
		GetQuery query = new GetQuery();
		query.setId(id);
		return Optional.ofNullable(elasticOperations.queryForObject(query, Album.class))
				.orElseThrow(() -> new DocumentNotFoundException("Document for id '%s' can not be found", id));
	}

	@Override
	public Page<Album> searchByAlbumName(String name, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("albumName", name))
				.withPageable(pageable).build();
		return elasticOperations.queryForPage(searchQuery, Album.class);
	}

	@Override
	public Page<Album> findByArtistId(String id, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(generateDefaultNestedQuery(id))
				.withPageable(pageable).build();
		return elasticOperations.queryForPage(searchQuery, Album.class);
	}

	@Override
	public List<Album> searchArtistAlbumsByGenre(String id, String genre) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(generateDefaultNestedQuery(id)).build();
		return elasticOperations.queryForList(searchQuery, Album.class).stream().filter(album->album.getGenre().contains(genre)).collect(Collectors.toList());
	}

	
	private NestedQueryBuilder generateDefaultNestedQuery(String artistId) {
	   return new NestedQueryBuilder(Artist.PATH,generateDefaultBoolQuery(artistId),org.apache.lucene.search.join.ScoreMode.None);
	}
	
	private BoolQueryBuilder generateDefaultBoolQuery(String artistId) {
		return QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("artists.artistId", artistId));
	}
	
}
