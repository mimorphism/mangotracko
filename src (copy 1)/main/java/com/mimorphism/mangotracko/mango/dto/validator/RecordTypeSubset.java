package com.mimorphism.mangotracko.mango.dto.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.mimorphism.mangotracko.mango.RecordType;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, 
	ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RecordTypeSubsetValidator.class)
public @interface RecordTypeSubset {

	RecordType[] anyOf();
    String message() default "must be any of {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
