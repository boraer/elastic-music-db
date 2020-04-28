package com.musicdb.albumservice.controller.config;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
	UNKNOWN_ERROR("UNKNOWN_ERROR", BAD_REQUEST), 
	DOCUMENT_NOT_FOUND("DOCUMENT_NOT_FOUND",NOT_FOUND),
	OPERATION_ERROR("OPERATION_ERROR",BAD_REQUEST),
	;
	
	final String key;
    final HttpStatus httpStatus;

    ErrorCode(String key, HttpStatus httpStatus) {
        this.key = key;
        this.httpStatus = httpStatus;
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
