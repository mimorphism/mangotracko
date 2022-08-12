package com.mimorphism.mangotracko.security.filter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimorphism.mangotracko.appuser.AppUser;
import com.mimorphism.mangotracko.appuser.AppUserService;
import com.mimorphism.mangotracko.exception.AlreadyLoggedinException;
import com.mimorphism.mangotracko.exception.BadLoginRequestException;
import com.mimorphism.mangotracko.exception.UserNotFoundException;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTService;
import com.mimorphism.mangotracko.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private static final String APPLICATION_JSON_VALUE = "Content-Type: application/json";

	private final AppUserService userService;
	
	private final ActiveJWTService activeJWTService;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager, AppUserService userService, ActiveJWTService activeJWTService) {
		this.authenticationManager = authenticationManager;
		this.setFilterProcessesUrl("/api/auth/login");
		this.userService = userService;
		this.activeJWTService = activeJWTService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> requestBody;
		try {
			requestBody = mapper.readValue(request.getInputStream(), Map.class);

			String password = requestBody.get("password");			
			AppUser user = userService.getUser(requestBody.get("username"));//User exists
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(), password);
			return authenticationManager.authenticate(authToken);
			
		}catch (NoSuchElementException e) {
			log.error("BAD INCOMING REQUEST: " + e);
			throw new BadLoginRequestException("Bad login request: Username and password fields not present");
		} catch (JsonParseException e) {
			log.error("BAD INCOMING REQUEST: " + e);
			throw new InternalAuthenticationServiceException(e.toString());
		} catch (JsonMappingException e) {
			log.error("BAD INCOMING REQUEST: " + e);
			throw new InternalAuthenticationServiceException(e.toString());
		} catch (IOException e) {
			log.error("BAD INCOMING REQUEST: " + e);
			throw new InternalAuthenticationServiceException(e.toString());
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		if (failed instanceof DisabledException) {
			SecurityUtil.writeResponse(response, FORBIDDEN,
					failed.getMessage() + ". Please confirm your registration");
		} else if (failed instanceof BadLoginRequestException) {
			log.error(failed.getMessage());
			SecurityUtil.writeResponse(response, BAD_REQUEST, failed.getMessage());

		}
		else if (failed instanceof UserNotFoundException) {
			log.error(String.format("User doesn't exist: {}", failed.getMessage()));
			SecurityUtil.writeResponse(response, BAD_REQUEST, String.format(failed.getMessage()));
		}
		else if (failed instanceof BadCredentialsException) {
			log.error(String.format("Incorrect password"));
			SecurityUtil.writeResponse(response, BAD_REQUEST, "Password is incorrect");

		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		try {
			AppUser user = (AppUser) authResult.getPrincipal();
			String accessToken = SecurityUtil.generateAccessToken(request, user);
			String refreshToken = SecurityUtil.generateRefreshToken(request, user);
			
			if (isAuthenticated(user.getUsername())) {
//				throw new AlreadyLoggedinException(String.format("User: %s - Your existing session has been terminated.", user.getUsername()), user.getUsername());
				log.info(String.format("User: %s - new login detected! existing session has been terminated.", user.getUsername()), user.getUsername());
				activeJWTService.removeActiveJWTForUser(user.getUsername());
			}

			// TODO jwt in cookie implementation - on hold for now
//			Cookie cookie = new Cookie(accessToken, "accessToken");
//			cookie.setPath("/");
//			cookie.setSecure(true);
//			cookie.setHttpOnly(true);
//			response.addCookie(cookie);
			
			activeJWTService.addActiveJWT(user.getUsername(), accessToken, refreshToken);
			
			Map<String, String> tokens = new HashMap<>();
			tokens.put("user", user.getUsername());
			tokens.put("accessToken", accessToken);
			tokens.put("refreshToken", refreshToken);
			response.setContentType(APPLICATION_JSON_VALUE);
			new ObjectMapper().writeValue(response.getOutputStream(), tokens);
		}catch(AlreadyLoggedinException ex) {
				log.error("Error logging in: {}", ex.getMessage());
				log.error("Proceeding to terminate existing session!");
				activeJWTService.removeActiveJWTForUser(((AlreadyLoggedinException) ex).getUser());
				SecurityUtil.writeResponse(response, OK, ex.getMessage() + 
						"You can login again to use the application." );
				}	
		}
		

	private boolean isAuthenticated(String username) {
		return activeJWTService.getActiveJWTForUser(username).isPresent();
	}

}
