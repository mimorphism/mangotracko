package com.mimorphism.mangotracko.mango.dto.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.mimorphism.mangotracko.mango.RecordType;

public class RecordTypeSubsetValidator implements ConstraintValidator<RecordTypeSubset, RecordType>{

	private RecordType[] subset;
	
	@Override
    public void initialize(RecordTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }


	@Override
	public boolean isValid(RecordType value, ConstraintValidatorContext context) {
		return value == null || Arrays.asList(subset).contains(value);
	}

}
