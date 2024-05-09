package com.hnguyen387.jpa_persistence.ch04.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UserValidation.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface ValidUser {
	String message() default "User data is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
