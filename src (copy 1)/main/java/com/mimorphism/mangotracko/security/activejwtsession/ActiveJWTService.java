package com.mimorphism.mangotracko.security.activejwtsession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveJWTService {
	
	@Autowired
	private ActiveJWTRepository activeJWTRepository;
	
	public void addActiveJWT(String username, String accessToken, String refreshToken) 
	{
		ActiveJWT jwt = new ActiveJWT(username, accessToken, refreshToken);
		activeJWTRepository.save(jwt);
	}
	
	public Optional<ActiveJWT> getActiveJWTForUser(String username) {
		return activeJWTRepository.findById(username);
	}
	
	public void removeActiveJWTForUser(String username) {
		Optional<ActiveJWT> jwt = activeJWTRepository.findById(username);
		
		if(jwt.isPresent()) {
			activeJWTRepository.deleteById(username);
		}
	}
	
	public void setActiveJWTForUser(String username, String accessToken, String refreshToken) {
		Optional<ActiveJWT> jwt = activeJWTRepository.findById(username);
		
		if(jwt.isPresent()) {
			jwt.get().setAccessToken(accessToken);
			jwt.get().setRefreshToken(refreshToken);
			activeJWTRepository.save(jwt.get());
		}
	}

}
