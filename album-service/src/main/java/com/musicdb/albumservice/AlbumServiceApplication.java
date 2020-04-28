package com.musicdb.albumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
@RibbonClient(name = "album-service") 
public class AlbumServiceApplication {

	
    public static void main(String[] args) {
        SpringApplication.run(AlbumServiceApplication.class, args);
    }

}
