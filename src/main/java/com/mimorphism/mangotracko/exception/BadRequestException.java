package com.mimorphism.mangotracko.exception;

import java.util.ArrayList;
import java.util.List;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -2576372693768965661L;
	
	private List<String> errorMsgs = new ArrayList<>();
	
	public BadRequestException(String msg) {
		super(msg);
	}
	
	
	public BadRequestException(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}
	
	public List<String> getErrorMsgs(){
		return errorMsgs;
	}
	

}