package ru.dudkomv.neoflexpractice.lesson.validation.startendtime;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartIsBeforeEndValidator.class)
public @interface StartIsBeforeEndConstraint {
    String message() default "The start date time of the lesson must be before the end date time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
