package com.mimorphism.mangotracko.exception;

public class RecordNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -8742422352808114437L;
	
	public RecordNotFoundException(String msg) {
		super(msg);
	}

}
