package com.cblandon.inversiones.excepciones.validation.validator;

import com.cblandon.inversiones.excepciones.validation.anotation.ValidDocument;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDocumentValidator implements ConstraintValidator<ValidDocument, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!value.matches("^\\d+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("incorrecto").addConstraintViolation();
            return false;
        }

        return true;
    }
}