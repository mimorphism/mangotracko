package com.mimorphism.mangotracko.exception;

import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedinException extends AuthenticationException{

	private static final long serialVersionUID = 8764309374960680820L;
	
	private final String USER;
	/**
	 * Constructs a <code>AlreadyLoggedinException</code> with the specified message.
	 * @param msg the detail message
	 */
	public AlreadyLoggedinException(String msg, String username) {
		super(msg);
		USER = username;
	}
	
	public String getUser() {
		return USER;
	}

}
