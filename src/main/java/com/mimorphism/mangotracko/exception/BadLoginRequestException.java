package com.mimorphism.mangotracko.exception;

import org.springframework.security.core.AuthenticationException;

public class BadLoginRequestException extends AuthenticationException{

	private static final long serialVersionUID = 5374121176987821296L;
	
	public BadLoginRequestException(String msg) {
		super(msg);
	}

}
