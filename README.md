# *Elastic-Music-DB with Spring 5*

A Starter Kit Project for Elastic Search 

## *Introduction*

The aim of this project is basically to show how to create, manage, update Elastic Search index, mappings or field search analyzer, autocomplete_filter. In addition, it is being showed how to keep structed data on Elastic Search or model the data

## *Getting Started*

### *Prerequisities*

 - [ ] Java 8+
 - [ ] Spring Framework 5+
 - [ ] Spring Boot 2.2+
 - [ ] Docker 19.3+
 - [ ] Elastic Search 6.8.8
 - [ ] Maven jib Plugin for dockerize the services
 

## *Running the Project*

artist-service

    mvn jib:dockerBuild

album-service

    mvn jib:dockerBuild
    
running  ElasticSearch and Services @Docker environment  

    docker-compose up
After running the project 
 - [ ] it is needed to create Album and Artist indexes via postman collection
 - [ ] It is needed to create indexes in 1 minute because services has a batch service which puts mapping  for objects 1 minute later services are started

## *Modeling the Data*

For this project, there are just 2 objects exist which are Artist and Album. Firstly, It was thought to use parent-child relationship but then realized that after Elastic Search v5.6 parent-child relations was removed. Therefore I modelled my data as 2 indexes which are Artist and Album but also Album has a partial version of Artist as a nested object. 

I tried to build up a hybrid denormalized-nested structure([For further reading elasticsearch how to model data](https://www.elastic.co/guide/en/elasticsearch/guide/master/modeling-your-data.html)). In album index, it is just kept id and name fields of artist. It is not so common that one artist would change its name or the record would be created wrong. In that cases Album index is need to be update.

In case of re-indexing issues, it might be designed a message or stream based pipeline between services and with one batch job can update the nested data in album if there  are exist name field updated artists.

## *Api*

**REST API**

 - [ ] /artists endpoint to manage Artist data. Required information:artist name
 - POST /artists to save a new artist
 - PUT /artists/{artistId} to update an existing artist
 - GET /artists lists all artists.

**Filtering by a part of artist name, sorting by artist name (asc/desc),paging applicable**

 - [ ] /artists/{artistId}/albums endpoint to manage Album data. Required information: title, year of release, genres (list of tags).
 - POST /artists/{artistId}/albums to add a new album to an existingartist
 - PUT /artists/{artistId}/albums/{albumId} to update an existing album
 - GET /artists/{artistId}/albums lists all albums by the given artist.

**Filtering by genre(s), sorting by album name (asc/desc) applicable**

### *Album Details*

For getting the album details and put it to the api call response. 3th party service [Discogs API has been used to get Album details with its search endpoint](https://www.discogs.com/developers#page:database,header:database-search)

## *Extra Work*

 1. Album details are get from Discogs Api
 2. with Aspect oriented programming  number of call ,average processing time per endpoint and time taken to Discogs Api is kept and logged
 3. infastructure works on Docker platform
 4. For learning purposes, I use ElasticSearch api to create and manage index. I also started project with spring-data-elasticsearch but i did not use boiler-plate jpa ElasticSearch Crud Repository. Instead I implemented my own repository classes with using elastich high rest client
 
 
 ## *Difficulties*
 
This was the first time I used Elasticsearch. Therefore, I needed to read documentation and also practice. In addition it was a bit hard to understand the data structure of ElasticSearch It was encountered that Elastic Search 7.6 has a bug with spring-data-elasticsearch and java elastic high rest client has a connection bug. So, it was downgraded to version 6.8.8 . In addition spring-data-elasticsearch does not have querydsl. So, restQuery can not be implementend

## Nice to Have(s)

Due to the bug of spring-data-elastic-search, Reactive implementation is left for now
 

  
