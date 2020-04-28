package com.musicdb.albumservice.exception;



public class ActionNotFoundException extends AbstractRuntimeException {

	public ActionNotFoundException(String message, Object... parameters) {
		super(message, parameters);
	}

	

}
