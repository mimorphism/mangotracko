package com.mimorphism.mangotracko.mango.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mimorphism.mangotracko.mango.RecordType;
import com.mimorphism.mangotracko.mango.dto.validator.RecordTypeSubset;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteRecordDTO {

	@NotNull
	@RecordTypeSubset(anyOf = {RecordType.BACKLOG, RecordType.CURRENTLY_READING, RecordType.FINISHED})
	private RecordType recordType;
	
	@NotNull
	@Min(1)
	private Long recordId;
	
	@NotNull
	@NotBlank(message = "Username is mandatory")
	@NotEmpty(message = "Username cannot be empty")
	private String user;
}
