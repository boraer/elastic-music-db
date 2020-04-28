package com.musicdb.albumservice.model;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = Album.INDEX_NAME)
public class Album {

	public static final String INDEX_NAME = "album";
	public static final String TYPE_NAME = "album";
	
	
	@Id String id;
	
	@NotNull
	@NotBlank
	@Field(fielddata = true,type = FieldType.Text)
	String albumName;
	
	@NotNull
	@Min(1500)
	@Max(2030)
	@Field(type = FieldType.Integer)
	Integer releaseYear;
	
	@Field(type = FieldType.Nested)
    @NotEmpty
	Set<Artist> artists;

	@Field(type = FieldType.Text)
	@NotEmpty
	String genre;
	
    
    Rating rating;
    
}
