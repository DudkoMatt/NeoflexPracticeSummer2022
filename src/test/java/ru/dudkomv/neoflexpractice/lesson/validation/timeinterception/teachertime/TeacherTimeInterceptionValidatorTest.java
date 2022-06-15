package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.teachertime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.TimeInterceptionCheckingService;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TeacherTimeInterceptionValidatorTest {
    @Mock
    private TimeInterceptionCheckingService timeCheckingService;

    @InjectMocks
    private TeacherTimeInterceptionValidator teacherTimeInterceptionValidator;

    @Test
    void isValid() {
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .startDateTime(null)
                .endDateTime(null)
                .build();

        assertTrue(teacherTimeInterceptionValidator.isValid(creationDto, context));
        Mockito.verify(timeCheckingService, Mockito.times(0)).doesNotIntersect(Mockito.any(), Mockito.eq(creationDto));

        creationDto.setStartDateTime(null);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertTrue(teacherTimeInterceptionValidator.isValid(creationDto, context));
        Mockito.verify(timeCheckingService, Mockito.times(0)).doesNotIntersect(Mockito.any(), Mockito.eq(creationDto));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(null);
        assertTrue(teacherTimeInterceptionValidator.isValid(creationDto, context));
        Mockito.verify(timeCheckingService, Mockito.times(0)).doesNotIntersect(Mockito.any(), Mockito.eq(creationDto));


        OffsetDateTime time = OffsetDateTime.of(LocalDateTime.of(2000, 1, 1, 1, 1), ZoneOffset.UTC);
        creationDto.setStartDateTime(time);
        creationDto.setEndDateTime(time);
        Boolean expected = false;

        Mockito.when(timeCheckingService.doesNotIntersect(Mockito.any(), Mockito.eq(creationDto))).thenReturn(expected);
        assertEquals(expected, teacherTimeInterceptionValidator.isValid(creationDto, context));
        Mockito.verify(timeCheckingService, Mockito.times(1)).doesNotIntersect(Mockito.any(), Mockito.eq(creationDto));
    }
}