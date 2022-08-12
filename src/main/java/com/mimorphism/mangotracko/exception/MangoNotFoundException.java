package com.mimorphism.mangotracko.exception;

public class MangoNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -4225584536782096504L;
	
	public MangoNotFoundException(String msg) {
		super(msg);
	}

}
