package com.musicdb.artistservice.exception;

public class DocumentNotFoundException extends AbstractRuntimeException {

	public DocumentNotFoundException(String message) {
		super(message);
	}

	public DocumentNotFoundException(String message, Object... parameters) {
		super(String.format(message, parameters));
	}
}
