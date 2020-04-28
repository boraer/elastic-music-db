package com.musicdb.artistservice.model;

import com.musicdb.artistservice.model.util.ArtistCategoryValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = Artist.INDEX_NAME)
public class Artist {

	public static final String INDEX_NAME = "artist";
	public static final String TYPE_NAME = "artist";
	
	@Id
	String id;

	@Field(analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search",type = FieldType.Text,fielddata = true)
	@NotNull
	String name;

	@NotNull
	String autobiography;

	@ArtistCategoryValidation(anyOf = { ArtistCategory.GROUP, ArtistCategory.INDIVIDUAL,ArtistCategory.CHOIR,ArtistCategory.ORCHESTRA })
	ArtistCategory artistCategory;

	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@Nullable
	String dateOfBirth;

	@Nullable
	String country;
    
	@Nullable
	String parentId;
}
