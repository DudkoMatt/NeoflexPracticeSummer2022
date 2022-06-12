package ru.dudkomv.neoflexpractice.teacher.validation.birthday;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Clock;
import java.time.LocalDate;

@RequiredArgsConstructor
public class IsBeforeTodayValidator implements ConstraintValidator<IsBeforeTodayConstraint, LocalDate> {
    private final Clock clock;

    @Override
    public boolean isValid(LocalDate providedDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate today = LocalDate.now(clock);
        return providedDate.isBefore(today);
    }
}
