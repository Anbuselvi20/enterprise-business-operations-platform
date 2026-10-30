package com.enterprise.employee.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class BcryptPasswordLengthValidator
        implements ConstraintValidator<BcryptPasswordLength, CharSequence> {

    @Override
    public boolean isValid(CharSequence password, ConstraintValidatorContext context) {
        return password == null
                || password.toString().getBytes(StandardCharsets.UTF_8).length <= 72;
    }
}
