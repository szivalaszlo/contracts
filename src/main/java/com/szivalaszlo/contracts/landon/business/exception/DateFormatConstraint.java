package com.szivalaszlo.contracts.landon.business.exception;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormatConstraint {

    String message() default "Invalid date format. Use yyyy/MM date format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
