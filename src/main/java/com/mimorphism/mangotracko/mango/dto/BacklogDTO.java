package com.mimorphism.mangotracko.mango.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mimorphism.mangotracko.mango.dto.validator.MangoDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BacklogDTO {
	
	@NotNull
	@NotBlank(message = "Mango title is mandatory")
	@NotEmpty(message = "Mango title cannot be empty")
	private String mangoTitle;
	
	@NotNull
	@NotBlank(message = "Added datetime is mandatory")
	@NotEmpty(message = "Added datetime must be ISO compliant and valid")
	@MangoDateTime
	private String addedDateTime;
	
	@NotNull
	@NotBlank(message = "Username is mandatory")
	@NotEmpty(message = "Username cannot be empty")
	private String user;
	
	@NotNull(message = "Anilist id is mandatory")
	@Min(1)
	private Long anilistId;

}
