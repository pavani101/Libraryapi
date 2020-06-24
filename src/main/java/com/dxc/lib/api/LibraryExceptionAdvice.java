package com.dxc.lib.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dxc.lib.exception.BookException;

@RestControllerAdvice
public class LibraryExceptionAdvice {
	
	@ExceptionHandler(BookException.class)
	public ResponseEntity<String> handleItemException(BookException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
