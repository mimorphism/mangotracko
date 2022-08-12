package com.mimorphism.mangotracko.exception;

public class MangoAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -950774294551467687L;
	
	public MangoAlreadyExistsException(String msg) {
		super(msg);
	}

}
