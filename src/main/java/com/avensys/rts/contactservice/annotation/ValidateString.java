package com.avensys.rts.contactservice.annotation;

import com.avensys.rts.contactservice.validator.StringArrayValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringArrayValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateString {
    String message() default "Invalid value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] acceptedValues();
}
