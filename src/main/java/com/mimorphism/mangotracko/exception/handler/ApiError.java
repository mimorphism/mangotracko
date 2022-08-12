package com.mimorphism.mangotracko.exception.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {

	   private HttpStatus status;
	   
	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	   private LocalDateTime timestamp;
	   private List<String> message;

	   private ApiError() {
	       timestamp = LocalDateTime.now();
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