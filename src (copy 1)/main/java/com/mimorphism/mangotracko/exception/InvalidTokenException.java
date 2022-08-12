package com.mimorphism.mangotracko.exception;

import org.springframework.security.access.AuthorizationServiceException;

public class InvalidTokenException extends AuthorizationServiceException{

	private static final long serialVersionUID = -4882454405207618914L;

	public InvalidTokenException(String msg) {
		super(msg);
	}
}
