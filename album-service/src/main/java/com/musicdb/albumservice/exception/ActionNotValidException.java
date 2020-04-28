package com.musicdb.albumservice.exception;

public class ActionNotValidException extends AbstractRuntimeException {

	public ActionNotValidException(String message, Object... parameters) {
		super(message, parameters);
		
	}

}
