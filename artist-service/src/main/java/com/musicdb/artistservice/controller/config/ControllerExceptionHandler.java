package com.musicdb.artistservice.controller.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.musicdb.artistservice.exception.DocumentNotFoundException;
import com.musicdb.artistservice.exception.DocumentOperationException;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity handleDocumentNotFoundException(final Exception ex, final WebRequest request) {
		log.warn("Exception Occured : ", ex);
		ApiError apiError = new ApiError(ErrorCode.DOCUMENT_NOT_FOUND, ex.getMessage());
		return new ResponseEntity<>(apiError, apiError.code.httpStatus);
	}

	@ExceptionHandler(DocumentOperationException.class)
	public ResponseEntity handleDocumetOperationException(final Exception ex, final WebRequest request) {
		log.warn("Exception Occured : ", ex);
		ApiError apiError = new ApiError(ErrorCode.OPERATION_ERROR, ex.getMessage());
		return new ResponseEntity<>(apiError, apiError.code.httpStatus);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleUnExpectedException(final Exception ex, final WebRequest request) {
		log.warn("Exception Occured : ", ex);
		ApiError apiError = new ApiError(ErrorCode.UNKNOWN_ERROR, ex.getMessage());
		return new ResponseEntity<>(apiError, apiError.code.httpStatus);
	}
	
	@Value
	protected static class ApiError {
		ErrorCode code;
		String message;
	}
}
