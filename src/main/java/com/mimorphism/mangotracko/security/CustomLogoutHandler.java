package com.mimorphism.mangotracko.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.exception.InvalidTokenException;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWT;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTRepository;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTService;
import com.mimorphism.mangotracko.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private ActiveJWTService activeJWTService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> requestBody;

		try {
			requestBody = mapper.readValue(request.getInputStream(), Map.class);

			String authHeader = request.getHeader(AUTHORIZATION);
			String refreshTokenHeader = request.getHeader(SecurityUtil.REFRESHER);
			if (authHeader != null && authHeader.startsWith(SecurityUtil.BEARER) && refreshTokenHeader != null
					&& authHeader.startsWith(SecurityUtil.BEARER)) {

				if (!(requestBody.get("username") instanceof String)) {
					SecurityUtil.writeResponse(response, BAD_REQUEST,
							"Doesn't seem like a valid username. String only pls");
					return;
				}

				String username = requestBody.get("username");

				if (userFieldDoesntExistInRequestBody(username)) {
					SecurityUtil.writeResponse(response, BAD_REQUEST, "User field not present");
					return;

				}
				if (userDoesntExist(username)) {
					SecurityUtil.writeResponse(response, BAD_REQUEST,
							"User doesn't exist, so obviously he's not logged in or out innit?");
					return;
				} else if (userExistsButNotInSession(username)) {
					SecurityUtil.writeResponse(response, BAD_REQUEST, "User is not in session");
					return;
				}

				else if (userExistsAndInSession(username)) {
					String accessToken = authHeader.substring(SecurityUtil.BEARER.length());
					String refreshToken = refreshTokenHeader.substring(SecurityUtil.BEARER.length());

					Optional<ActiveJWT> activeJWT = activeJWTService.getActiveJWTForUser(username);
					if (activeJWT.isPresent() && activeJWT.get().getAccessToken().equals(accessToken)
							&& activeJWT.get().getRefreshToken().equals(refreshToken)) {
						activeJWTService.removeActiveJWTForUser(username);
						SecurityUtil.writeResponse(response, OK, "logout succesful!");
					} else {
						throw new InvalidTokenException("Invalid token");
					}
				}
			}
		} catch (InvalidTokenException ex) {
			SecurityUtil.writeResponse(response, BAD_REQUEST, ex.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (TokenExpiredException ex) {
			log.error("Authorization error: {}", ex.getMessage());
			SecurityUtil.writeResponse(response, UNAUTHORIZED, ex.getMessage());
	}}

	private boolean userFieldDoesntExistInRequestBody(String username) {
		return username == null;
	}

	private boolean userDoesntExist(String username) {
		return username != null && appUserService.getUser(username) == null;//TODO temporary, need to handle exception
	}

	private boolean userExistsButNotInSession(String username) {
		return appUserService.getUser(username) != null //TODO temporary, need to handle exception
				&& !activeJWTService.getActiveJWTForUser(username).isPresent();
	}

	private boolean userExistsAndInSession(String username) {
		return appUserService.getUser(username) != null//TODO temporary, need to handle exception
				&& activeJWTService.getActiveJWTForUser(username).isPresent();
	}

}
