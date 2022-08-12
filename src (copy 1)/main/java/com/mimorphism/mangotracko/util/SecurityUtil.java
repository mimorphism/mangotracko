package com.mimorphism.mangotracko.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.exception.handler.ApiError;

public final class SecurityUtil {

	private SecurityUtil() {};
	
	public static final String LOGIN_PATH = "/api/auth/login";
	public static final String REFRESH_TOKEN_PATH = "/api/user/refreshtoken";
	public static final String REGISTRATION_PATH = "/api/registration";
	public static final String BEARER = "Bearer ";
	public static final String REFRESHER = "Refresher";

	
	public static Algorithm getAlgorithm() 
	{
		return Algorithm.HMAC256("secret".getBytes());
	}
	
	public static void writeResponse(HttpServletResponse response, HttpStatus statusCode, String message)
		 {
		ApiError responseBody = new ApiError(statusCode, message);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(statusCode.value());
		try {
			new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//7800 access token, 8800 refresh token
	public static String generateAccessToken(HttpServletRequest request, AppUser user) {
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 7800 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(getAlgorithm());
		return accessToken;
	}
	
	public static String generateRefreshToken(HttpServletRequest request, AppUser user) {
		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 8800 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(getAlgorithm());
		return refreshToken;
	}

	
}
