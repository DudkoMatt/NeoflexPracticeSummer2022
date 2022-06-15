package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.LessonRepository;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TimeInterceptionCheckingServiceTest {
    private final LocalDateTime time0 = LocalDateTime.of(2022, 1, 1, 1, 0);
    private final LocalDateTime time1 = LocalDateTime.of(2022, 1, 2, 1, 0);
    private final LocalDateTime time2 = LocalDateTime.of(2022, 1, 3, 1, 0);
    private final LocalDateTime time3 = LocalDateTime.of(2022, 1, 4, 1, 0);
    private final LocalDateTime time4 = LocalDateTime.of(2022, 1, 5, 1, 0);
    private final LocalDateTime time5 = LocalDateTime.of(2022, 1, 6, 1, 0);
    private final LocalDateTime time6 = LocalDateTime.of(2022, 1, 7, 1, 0);

    /*
    Check cases

        0  1 2    3    4  5    6
    ---------|++++++++++++|----------

    ----|--|-------------------------  1
    ----|----|-----------------------  2
    ----|---------|------------------  3
    ----|-----------------|----------  4
    ----|----------------------|-----  5

    ---------|---------|-------------- 6
    ---------|------------|----------  7
    ---------|-----------------|-----  8

    --------------|----|-------------  9
    --------------|-------|----------  10
    --------------|------------|-----  11

    ---------------------|-----|-----  12

     */

    @InjectMocks
    private TimeInterceptionCheckingService timeInterceptionCheckingService;

    @Test
    void doesNotIntersect() {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .startDateTime(convert(time1))
                .endDateTime(convert(time2))
                .build();

        Lesson lesson = Lesson.builder()
                .startDateTime(time2)
                .endDateTime(time5)
                .build();

        Function<LessonRepository, Lesson> getLessonFunction = lessonRepository -> null;
        assertTrue(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        getLessonFunction = lessonRepository -> lesson;


        // 1
        setTime(creationDto, time0, time1);
        assertTrue(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 2
        setTime(creationDto, time0, time2);
        assertTrue(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 3
        setTime(creationDto, time0, time3);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 4
        setTime(creationDto, time0, time5);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 5
        setTime(creationDto, time0, time6);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 6
        setTime(creationDto, time2, time4);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 7
        setTime(creationDto, time2, time5);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 8
        setTime(creationDto, time2, time6);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 9
        setTime(creationDto, time3, time4);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 10
        setTime(creationDto, time3, time5);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 11
        setTime(creationDto, time3, time6);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));

        // 12
        setTime(creationDto, time5, time6);
        assertFalse(timeInterceptionCheckingService.doesNotIntersect(getLessonFunction, creationDto));
    }

    private static void setTime(LessonCreationDto creationDto, LocalDateTime start, LocalDateTime end) {
        creationDto.setStartDateTime(convert(start));
        creationDto.setEndDateTime(convert(end));
    }

    private static OffsetDateTime convert(LocalDateTime localDateTime) {
        return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}