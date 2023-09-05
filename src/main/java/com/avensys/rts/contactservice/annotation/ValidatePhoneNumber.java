package com.avensys.rts.contactservice.annotation;

import com.avensys.rts.contactservice.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { PhoneNumberValidator.class })
@Documented
public @interface  ValidatePhoneNumber {

    String message() default "Invalid phone number";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}

