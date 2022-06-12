package ru.dudkomv.neoflexpractice.teacher.validation.birthday;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsBeforeTodayValidator.class)
public @interface IsBeforeTodayConstraint {
    String message() default "The provided date must be in the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
