package com.mimorphism.mangotracko.registration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    
	@NotNull
	@NotBlank
	private final String username;
    
	@NotNull
	@NotBlank
	private final String password;
}