package com.hnguyen387.jpa_persistence.globalexceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptions {
	
	private ResponseEntity<ErrorDetails> handleException(RuntimeException e, HttpStatus status) {
		ErrorDetails error = new ErrorDetails();
		error.setCode(status.value());
		error.setStatus(status.name());
		error.setErrMessage(e.getMessage());
		error.setDateTime(LocalDateTime.now());
		return new ResponseEntity<ErrorDetails>(error, status);
	}
	@ExceptionHandler(SavedException.class)
	public ResponseEntity<ErrorDetails> handleSavedException(SavedException exception) {
		return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(UpdatedException.class)
	public ResponseEntity<ErrorDetails> handleUpdatedException(UpdatedException exception) {
		return handleException(exception, HttpStatus.NOT_MODIFIED);
	}
	@ExceptionHandler(ImportException.class)
	public ResponseEntity<ErrorDetails> handleExcelException(ImportException exception) {
		return handleException(exception, HttpStatus.EXPECTATION_FAILED);
	}
}
