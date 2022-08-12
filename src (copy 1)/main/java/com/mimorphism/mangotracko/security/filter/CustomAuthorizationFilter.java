package com.mimorphism.mangotracko.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimorphism.mangotracko.exception.InvalidTokenException;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWT;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTRepository;
import com.mimorphism.mangotracko.security.activejwtsession.ActiveJWTService;
import com.mimorphism.mangotracko.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static java.util.Arrays.stream;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private final ActiveJWTService activeJWTService;

	public CustomAuthorizationFilter(ActiveJWTService activeJWTService) {
		this.activeJWTService = activeJWTService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().equals(SecurityUtil.LOGIN_PATH)
				|| request.getServletPath().equals(SecurityUtil.REFRESH_TOKEN_PATH)
				|| request.getServletPath().equals(SecurityUtil.REGISTRATION_PATH)) {
			filterChain.doFilter(request, response);
		} else {
			String authHeader = request.getHeader(AUTHORIZATION);
			if (authHeader != null && authHeader.startsWith(SecurityUtil.BEARER)) {
				try {
					String token = authHeader.substring(SecurityUtil.BEARER.length());

					Algorithm algo = SecurityUtil.getAlgorithm();
					JWTVerifier verifier = JWT.require(algo).build();
					DecodedJWT decodedJWT = verifier.verify(token);

					String username = decodedJWT.getSubject();
					long start = System.currentTimeMillis();
					log.info("start: " + start);
					if (activeJWTService.getActiveJWTForUser(username).isPresent()
							&& activeJWTService.getActiveJWTForUser(username).get().getAccessToken().equals(token)) {
						String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
						Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
						stream(roles).forEach(role -> {
							authorities.add(new SimpleGrantedAuthority(role));
						});
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								username, null, authorities);
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
						filterChain.doFilter(request, response);
						
					}else 
					{
						long finish = System.currentTimeMillis();
						log.info("finish: " + finish);
						log.info(String.valueOf(finish - start));
						throw new InvalidTokenException(String
								.format("Authorization attempt with invalid access token! User:%s", username));
					}

					
				} catch (TokenExpiredException ex) {
					log.error("Authorization error: {}", ex.getMessage());
					SecurityUtil.writeResponse(response, UNAUTHORIZED, ex.getMessage());
				} catch (InvalidTokenException ex) {
					log.error("Authorization error: {}", ex.getMessage());
					SecurityUtil.writeResponse(response, FORBIDDEN, ex.getMessage());
				}
				catch (SignatureVerificationException ex) {
					log.error("Authorization error: {}", ex.getMessage());
					SecurityUtil.writeResponse(response, FORBIDDEN, "Invalid token!");
				}

			} else {
				filterChain.doFilter(request, response);

			}
		}
	}

}
