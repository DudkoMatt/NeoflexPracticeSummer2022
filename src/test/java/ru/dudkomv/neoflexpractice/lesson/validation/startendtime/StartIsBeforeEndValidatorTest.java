package ru.dudkomv.neoflexpractice.lesson.validation.startendtime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

import javax.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartIsBeforeEndValidatorTest {

    @Test
    void isValid() {
        StartIsBeforeEndValidator validator = new StartIsBeforeEndValidator();
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        LessonCreationDto creationDto = LessonCreationDto.builder()
                .startDateTime(null)
                .endDateTime(null)
                .build();
        assertTrue(validator.isValid(creationDto, context));

        creationDto.setStartDateTime(null);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertTrue(validator.isValid(creationDto, context));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(null);
        assertTrue(validator.isValid(creationDto, context));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertFalse(validator.isValid(creationDto, context));

        creationDto.setStartDateTime(OffsetDateTime.of(2023, 5, 20, 20, 0,0,0, ZoneOffset.UTC));
        creationDto.setEndDateTime(OffsetDateTime.of(2022, 5, 20, 20, 0,0,0, ZoneOffset.UTC));
        assertFalse(validator.isValid(creationDto, context));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(OffsetDateTime.MAX);
        assertTrue(validator.isValid(creationDto, context));
    }
}