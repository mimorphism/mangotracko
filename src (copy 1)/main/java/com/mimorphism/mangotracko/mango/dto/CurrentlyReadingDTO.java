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
public class CurrentlyReadingDTO {
	
	@NotNull
	@NotBlank(message = "Mango title is mandatory")
	@NotEmpty(message = "Mango title cannot be empty")
	private String mangoTitle;
	
	@NotNull
	@Min(0)
	private Integer lastChapterRead;
	
	@NotNull
	@NotBlank(message = "Last read time datetime is mandatory")
	@NotEmpty(message = "Last read time datetime cannot be empty")
	@MangoDateTime
	private String lastReadTime;
	
	@NotNull
	@NotBlank(message = "Username is mandatory")
	@NotEmpty(message = "Username cannot be empty")
	private String user;
	
	@NotNull(message = "Anilist id is mandatory")
	@Min(1)
	private Long anilistId;
	
	private String remarks;
	
//	public CurrentlyReadingDTO(String mangoTitle, Integer lastChapterRead, String lastReadTime, String user,
//			Long anilistId) {
//		this.mangoTitle = mangoTitle;
//		this.lastChapterRead = lastChapterRead;
//		this.lastReadTime = lastReadTime;
//		this.user = user;
//		this.anilistId = anilistId;
//	}
	
}
