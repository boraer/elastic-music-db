package com.musicdb.artistservice.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ElasticConfiguration extends AbstractElasticsearchConfiguration{

	@Value("${app.elastic.host}")
	String host;
	
	@Value("${app.elastic.port}")
	Integer port;
	
	@Value("${app.elastic.portAlternate}")
	Integer portAlternate;
	
	@Override
	public RestHighLevelClient elasticsearchClient() {
		
		RestHighLevelClient client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost(host, port, "http"),
		                new HttpHost(host, port, "http")));
	return client;
	}
	
	@Bean 
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}
	
}