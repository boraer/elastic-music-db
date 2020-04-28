package com.musicdb.albumservice.service.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.musicdb.albumservice.exception.ActionNotFoundException;
import com.musicdb.albumservice.exception.ActionNotValidException;
import com.musicdb.albumservice.model.Album;
import com.musicdb.albumservice.model.Artist;
import com.musicdb.albumservice.model.Comment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActionValidatorService {

	private ObjectMapper objectMapper;

	@Autowired
	public ActionValidatorService(ObjectMapper objectMapper) {
		super();
		this.objectMapper = objectMapper;
	}

	public Map<ActionType, String> deserializeAction(String actionsStr) throws Exception {
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        List<Action> actions = objectMapper.reader()
        	      .forType(new TypeReference<List<Action>>() {})
        	      .readValue(actionsStr);
      
		Map<ActionType, String> objectList = new HashMap<ActionType, String>();
		for (Action action : actions) {
			  Map<String, Object> map 
		        = objectMapper.readValue(action.getObject(), new TypeReference<Map<String,Object>>(){});
			String actionName = null;
			
			actionName = (String) action.getAction();
			if (actionName == null)
				throw new ActionNotFoundException("Action is not found");

			ActionType actionType = ActionType.getByKey(actionName);
			if (actionType == null)
				throw new ActionNotValidException("Action(%s) is not a valid action", actionName);

			String object = action.getObject();
			if (object == null)
				throw new ActionNotValidException("Action(%s) has not a valid action object", actionName);

			objectList.put(actionType,object);
			
			
		}
		return objectList;
	}

	public Album buildUpdateObject(Album album, String actions) throws Exception {

		Map<ActionType, String> updateObjects = deserializeAction(actions);

		for (Entry<ActionType, String> object : updateObjects.entrySet()) {
			switch (object.getKey()) {
			case ADD_ARTIST_ACTION:
			//	album.getArtists().add(artist);
				break;
			case REMOVE_ARTIST_ACTION:
				final String artistId = (String) object.getValue();
				List<Artist> oldArtist = album.getArtists().stream().filter(a -> a.getArtistId().equals(artistId))
						.collect(Collectors.toList());
				album.getArtists().removeAll(oldArtist);
				break;
			case ADD_GENRES_ACTION:
				List<String> genres = Arrays.asList(album.getGenre().split(" "));
				final String newGenre = (String) object.getValue();
				genres.add(newGenre);
				StringBuffer buffer = new StringBuffer();
				genres.forEach(g -> buffer.append(g + " "));
				album.setGenre(buffer.toString());
				break;
			case REMOVE_GENRES_ACTION:
				List<String> genress = Arrays.asList(album.getGenre().split(" "));
				final String oldGenre = (String) object.getValue();
				genress.remove(oldGenre);
				StringBuffer bufferr = new StringBuffer();
				genress.forEach(g -> bufferr.append(g + " "));
				album.setGenre(bufferr.toString());
				break;
			}
		}

		return album;
	}

}
