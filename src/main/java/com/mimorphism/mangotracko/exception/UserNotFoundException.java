package com.mimorphism.mangotracko.exception;
import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException{

	private static final long serialVersionUID = -5688535329211292402L;

	public UserNotFoundException(String msg) {
		super(msg);
	}

}
