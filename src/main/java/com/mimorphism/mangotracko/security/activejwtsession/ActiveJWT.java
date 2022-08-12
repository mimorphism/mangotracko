package com.mimorphism.mangotracko.security.activejwtsession;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@RedisHash("ActiveJWT")
@Data
public class ActiveJWT implements Serializable {

	private static final long serialVersionUID = -5121940616187460920L;
	private String id;
	private String accessToken;
	private String refreshToken;
	
	public ActiveJWT(String id, String accessToken, String refreshToken) 
	{
		this.id = id;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
