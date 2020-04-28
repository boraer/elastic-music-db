package com.musicdb.albumservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class AlbumServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbumServiceApplication.class, args);
    }

}
