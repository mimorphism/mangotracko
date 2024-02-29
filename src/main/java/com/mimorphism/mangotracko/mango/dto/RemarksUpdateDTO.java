package com.mimorphism.mangotracko.mango.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemarksUpdateDTO {
	private String remarks;
	
	@NotNull(message = "Anilist id is mandatory")
	@Min(1)
	private Long anilistId;

}
