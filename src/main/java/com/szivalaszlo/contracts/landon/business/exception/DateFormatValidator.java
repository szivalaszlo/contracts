package com.szivalaszlo.contracts.landon.business.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateFormatValidator implements ConstraintValidator<DateFormatConstraint, String> {


    @Override
    public void initialize(DateFormatConstraint dateFormatConstraint) {

    }

    @Override
    public boolean isValid(String dateFormatField, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate date = LocalDate.parse(dateFormatField);
            return true;
        } catch (Exception e) {
            if (dateFormatField == null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
