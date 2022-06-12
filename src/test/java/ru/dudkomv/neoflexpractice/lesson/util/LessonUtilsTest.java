package ru.dudkomv.neoflexpractice.lesson.util;

import org.junit.jupiter.api.Test;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LessonUtilsTest {

    @Test
    void startOrEndTimeIsNull() {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .startDateTime(null)
                .endDateTime(null)
                .build();
        assertTrue(LessonUtils.startOrEndTimeIsNull(creationDto));

        creationDto.setStartDateTime(null);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertTrue(LessonUtils.startOrEndTimeIsNull(creationDto));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(null);
        assertTrue(LessonUtils.startOrEndTimeIsNull(creationDto));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertFalse(LessonUtils.startOrEndTimeIsNull(creationDto));
    }

    @Test
    void startAndEndTimeIsNotNull() {
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .startDateTime(null)
                .endDateTime(null)
                .build();
        assertFalse(LessonUtils.startAndEndTimeIsNotNull(creationDto));

        creationDto.setStartDateTime(null);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertFalse(LessonUtils.startAndEndTimeIsNotNull(creationDto));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(null);
        assertFalse(LessonUtils.startAndEndTimeIsNotNull(creationDto));

        creationDto.setStartDateTime(OffsetDateTime.MIN);
        creationDto.setEndDateTime(OffsetDateTime.MIN);
        assertTrue(LessonUtils.startAndEndTimeIsNotNull(creationDto));
    }
}