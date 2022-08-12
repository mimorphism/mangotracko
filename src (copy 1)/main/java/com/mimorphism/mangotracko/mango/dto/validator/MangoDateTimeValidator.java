package com.mimorphism.mangotracko.mango.dto.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.mimorphism.mangotracko.util.MangoUtil;

public class MangoDateTimeValidator implements ConstraintValidator<MangoDateTime, String>{

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(MangoUtil.APP_ISO_DATETIME_FORMAT);
	
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			if(value == null || value.isEmpty()) {return false;}
            this.dateTimeFormatter.parse(value);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
	}

}
