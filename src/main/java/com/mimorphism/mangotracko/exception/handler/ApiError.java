package com.mimorphism.mangotracko.exception.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {

	   private HttpStatus status;
	
	   private String timestamp;
	   
	   private List<String> message;

	   private ApiError() {
		  this.timestamp =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"));
	   }
	   
	   public ApiError(HttpStatus status, String message) {
	       this();
	       this.status = status;
	       this.message = new ArrayList<>();
	       this.message.add(message);
	   }
	   
	   public ApiError(HttpStatus status, List<String> message) {
	       this();
	       this.status = status;
	       this.message = message;
	   }


}