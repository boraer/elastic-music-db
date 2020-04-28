package com.musicdb.artistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class ArtistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtistServiceApplication.class, args);
	}

}
