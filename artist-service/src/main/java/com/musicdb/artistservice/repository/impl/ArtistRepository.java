package com.musicdb.artistservice.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.musicdb.artistservice.exception.DocumentNotFoundException;
import com.musicdb.artistservice.exception.DocumentOperationException;
import com.musicdb.artistservice.model.Artist;
import com.musicdb.artistservice.repository.IArtistRepository;

@Repository
public class ArtistRepository implements IArtistRepository {

	private ObjectMapper objectMapper;

	private ElasticsearchOperations elasticOperations;

	@Autowired
	public ArtistRepository(ObjectMapper objectMapper, ElasticsearchOperations elasticsearchOperations) {
		this.objectMapper = objectMapper;
		this.elasticOperations = elasticsearchOperations;
	}

	@Override
	public Page<Artist> findAll(Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery())
				.withPageable(pageable).build();

		return elasticOperations.queryForPage(searchQuery, Artist.class);
	}

	@Override
	public Page<Artist> search(String keyword, Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", keyword))
				.withPageable(pageable).build();
		return elasticOperations.queryForPage(searchQuery, Artist.class);

	}

	@Override
	public Artist saveArtist(Artist artist, Boolean refresh) {
		try {
			final IndexQuery indexQuery = new IndexQueryBuilder().withIndexName(Artist.INDEX_NAME)
					.withType(Artist.TYPE_NAME).withSource(objectMapper.writeValueAsString(artist)).build();
			final String id = elasticOperations.index(indexQuery);
			if (refresh)
				elasticOperations.refresh(Artist.INDEX_NAME);

			artist.setId(id);
			return artist;
		} catch (Exception e) {
			throw new DocumentOperationException("Add operation has an exception: " + e);
		}
	}

	@Override
	public Artist updateArtist(Artist artist, Boolean refresh) {
		String entity;
		try {
			entity = objectMapper.writeValueAsString(artist);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new DocumentOperationException("Object parse error");
		}

		UpdateRequest updateRequest = new UpdateRequest(Artist.INDEX_NAME, Artist.TYPE_NAME, artist.getId());
		updateRequest.doc(entity, XContentType.JSON);
		updateRequest.fetchSource(true);
		UpdateQuery updateQuery = new UpdateQueryBuilder().withIndexName(Artist.INDEX_NAME).withType(Artist.TYPE_NAME)
				.withId(artist.getId()).withUpdateRequest(updateRequest).build();
		UpdateResponse response = elasticOperations.update(updateQuery);

		if (refresh)
			elasticOperations.refresh(Artist.INDEX_NAME);

		switch (response.getResult()) {
		case UPDATED:
			return artist;
		case NOT_FOUND:
			throw new DocumentNotFoundException("Document(Artist) with id '%s' can not be found for Update",
					artist.getId());
		default:
			throw new DocumentOperationException("Wrong Operation has been executed for Document(Artist) with id '%s'",
					artist.getId());

		}
	}

	@Override
	public Artist findById(String id) {
		GetQuery query = new GetQuery();
		query.setId(id);
		return Optional.ofNullable(elasticOperations.queryForObject(query, Artist.class))
				.orElseThrow(() -> new DocumentNotFoundException("Document for id '%s' can not be found", id));

	}

	private List<Artist> getSearchResult(SearchResponse response) {

		SearchHit[] searchHit = response.getHits().getHits();
		List<Artist> profileDocuments = new ArrayList<>();

		if (searchHit.length > 0) {

			Arrays.stream(searchHit).forEach(
					hit -> profileDocuments.add(objectMapper.convertValue(hit.getSourceAsMap(), Artist.class)));
		}

		return profileDocuments;
	}

	@Override
	public Page<Artist> searchAndFilter(String parentId, String name, Pageable pageable) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("parentId", parentId))
					.filter(QueryBuilders.matchQuery("name", name));
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
			return elasticOperations.queryForPage(searchQuery, Artist.class);
		}
}
