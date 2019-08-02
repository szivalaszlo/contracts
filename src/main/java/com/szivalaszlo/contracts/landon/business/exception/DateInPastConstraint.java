package com.szivalaszlo.contracts.landon.business.exception;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateInPastValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateInPastConstraint {

    String message() default "Date must be in the past!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
