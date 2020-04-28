package com.musicdb.albumservice.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import com.musicdb.albumservice.model.Album;

@Component
public class PostMappings {

	
	private  ElasticsearchOperations elasticsearchOperations;
     
	@Autowired
	public PostMappings( ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@PostConstruct
	public void mapping() {
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		elasticsearchOperations.putMapping(Album.class);
	}

	
}
