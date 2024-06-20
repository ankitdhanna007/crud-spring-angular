package com.example.demo.dto.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Year;

@Constraint(validatedBy = CustomYearValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYear {
    String message() default "The year must be within the last given no. of years";
    Class<?>[] groups() default {};
    int years() default 20;  // Default to 20 years if not specified
    Class<? extends Payload>[] payload() default {};
}


class CustomYearValidator implements ConstraintValidator<ValidYear, Integer> {

    private int years;

    @Override
    public void initialize(ValidYear constraintAnnotation) {
        this.years = constraintAnnotation.years();
    }

    @Override
    public boolean isValid(Integer yearField, ConstraintValidatorContext context) {
        if (yearField == null) {
            return false;
        }
        int currentYear = Year.now().getValue();
        int lowerBoundYear = currentYear - years;
        return yearField >= lowerBoundYear && yearField <= currentYear;
    }
}