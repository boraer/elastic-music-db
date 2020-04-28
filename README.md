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
 - [ ] It is needed to create indexes in 1 minute because services has a batch service which puts mapping object for objects 1 minute later services is started

## *Modeling the Data*

For this project, there are just 2 objects exist which are Artist and Album. Firstly, It was thought to use parent-child relationship but then realized that after Elastic Search v5.6 parent-child relations was removed. Therefore I modelled my data as 2 indexes which are Artist and Album but also Album has a partial version of Artist as a nested object. 

I tried to build up a hybrid denormalized-nested structure([For further reading elasticsearch how to model data](https://www.elastic.co/guide/en/elasticsearch/guide/master/modeling-your-data.html)). In album index, it is just kept id and name fields of artist. It is not so common that one artist would change its name or the record would be created wrong. In that cases Album index is need to be update.

In case of re-indexing issues, it might be designed a message or stream based pipeline between services and with one batch job can update the nested data in album if there  are exist name field updated artists.
