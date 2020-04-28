package com.musicdb.albumservice.service.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;
import com.musicdb.albumservice.model.*;

public enum ActionType {

	ADD_ARTIST_ACTION("addArtist","artist",Artist.class),
	REMOVE_ARTIST_ACTION("removeArtist","artist",String.class),
	ADD_GENRES_ACTION("addGenres","genre",String.class),
	REMOVE_GENRES_ACTION("removeGenres","genre",String.class),
	ADD_COMMENT_ACTION("addCommentAction","comment",Comment.class),
	REMOVE_COMMENT_ACTION("removeCommentAction","comment",Comment.class),
	;
	
	private static final Map<String,ActionType> KEYSET = Collections.unmodifiableMap(new HashMap<String, ActionType>() {{
          Arrays.stream(ActionType.values()).forEachOrdered(action->put(action.key,action));
	}});
	
	private ActionType(String key,String field,Class clazz) {
		this.key = key;
		this.field = field;
		this.clazz = clazz;
	}

	private String key;
    private String field;
    private Class clazz;
	
    @JsonValue
	public String getKey() {
		return key;
	}

	public String getField() {
		return field;
	}

	public Class getClazz() {
		return clazz;
	}
	
	public static ActionType getByKey(final String key) {
		return KEYSET.get(key);
	}
    
}
