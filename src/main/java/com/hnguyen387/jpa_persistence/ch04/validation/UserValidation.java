package com.hnguyen387.jpa_persistence.ch04.validation;

import java.time.LocalDate;
import java.util.regex.Pattern;

import com.hnguyen387.jpa_persistence.ch04.dtos.ImportedUser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserValidation implements ConstraintValidator<ValidUser, ImportedUser>{
	private static final String EMAIL_REGEX = 
			"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	@Override
	public boolean isValid(ImportedUser user, ConstraintValidatorContext context) {
		boolean valid = true;
		if (user != null) {
			String username = user.getUsername();
			boolean emailExist = user.isEmailExist();
			String email = user.getEmail();
			LocalDate registrationDate = user.getRegistrationDate();
			int level = user.getLevel();
			if (username == null || username.isEmpty()) {
				addViolation(context, "Username cannot be empty.");
				valid = false;
			}
			if (registrationDate == null) {
				addViolation(context, "RegistrationDate cannot be empty.");
				valid = false;
			}
			if (emailExist) {
				addViolation(context, "Email has been registered.");
				valid = false;
			} else {
				boolean isMatch = Pattern.compile(EMAIL_REGEX).matcher(email).matches();
				if (!isMatch) {
					addViolation(context, email + " is not a valid email address.");
					valid = false;
				}
			}
			if (level < 1 && level > 5) {
				addViolation(context, "Level must be in range (1-5)");
				valid = false;
			}
		}
		return valid;
	}
	private void addViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation()
               .disableDefaultConstraintViolation();
    }
}
