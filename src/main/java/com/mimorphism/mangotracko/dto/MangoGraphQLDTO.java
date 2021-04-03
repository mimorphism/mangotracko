package com.mimorphism.mangotracko.dto;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mimorphism.mangotracko.model.OngoingMango;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MangoGraphQLDTO {

	private String mangoQuery;
}
