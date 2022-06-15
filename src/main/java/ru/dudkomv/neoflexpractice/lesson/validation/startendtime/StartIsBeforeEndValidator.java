package ru.dudkomv.neoflexpractice.lesson.validation.startendtime;

import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static ru.dudkomv.neoflexpractice.lesson.util.LessonUtils.startOrEndTimeIsNull;

public class StartIsBeforeEndValidator implements ConstraintValidator<StartIsBeforeEndConstraint, LessonCreationDto> {
    @Override
    @SuppressWarnings({"ConstantConditions", "java:S2259"})
    public boolean isValid(LessonCreationDto creationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (startOrEndTimeIsNull(creationDto)) {
            return true;
        }

        return creationDto
                .getStartDateTime()
                .isBefore(
                        creationDto.getEndDateTime()
                );
    }
}
