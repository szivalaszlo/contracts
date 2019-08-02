package com.szivalaszlo.contracts.landon.business.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateInPastValidator implements ConstraintValidator<DateInPastConstraint, String> {

    @Override
    public void initialize(DateInPastConstraint dateInPastConstraint) {

    }

    @Override
    public boolean isValid(String dateField, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate date = LocalDate.parse(dateField);
            LocalDate now = LocalDate.now();
            if (date.isBefore(now)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (dateField == null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
