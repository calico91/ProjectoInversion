package com.cblandon.inversiones.excepciones.validation.validator;

import com.cblandon.inversiones.excepciones.validation.anotation.ValidNullBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidNullBlankValidator implements ConstraintValidator<ValidNullBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {


        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("No debe ser null").addConstraintViolation();
            return false;
        }
        if (value.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("No puede estar vacio").addConstraintViolation();
            return false;
        }


        return true;
    }
}
