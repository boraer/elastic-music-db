package com.musicdb.artistservice.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import com.musicdb.artistservice.model.Artist;

@Component
public class PostMappings {

	
	private  ElasticsearchOperations elasticsearchOperations;
     
	@Autowired
	public PostMappings( ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}
	
	@PostConstruct
	public void mapping() {
		elasticsearchOperations.putMapping(Artist.class);
	}

	
}
