package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.coursetime;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CourseTimeInterceptionValidator.class)
public @interface CourseTimeInterceptionConstraint {
    String message() default "Lesson time intercepts with some another course lesson";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
