package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.teachertime;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TeacherTimeInterceptionValidator.class)
public @interface TeacherTimeInterceptionConstraint {
    String message() default "Lesson time intercepts with some teacher's lesson";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
