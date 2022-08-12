package com.mimorphism.mangotracko.exception.handler;

import org.springframework.http.ResponseEntity;

public class ResponseHandler {
	
	public static ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
	       return new ResponseEntity<>(apiError, apiError.getStatus());
	   }

}
