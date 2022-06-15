package ru.dudkomv.neoflexpractice.lesson.mapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LessonMapperTest {
    @Test
    void toLocalDateTime() {
        OffsetDateTime offsetDateTime = OffsetDateTime.of(1970, 1, 1, 15, 0, 0,0, ZoneOffset.ofHours(3));
        LocalDateTime expected = LocalDateTime.of(1970, 1, 1, 12, 0, 0, 0);

        LocalDateTime actual = LessonMapper.toLocalDateTime(offsetDateTime);
        assertEquals(expected, actual);

        assertNull(LessonMapper.toLocalDateTime(null));
    }

    @Test
    void toOffsetDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(1970, 1, 1, 12, 0, 0, 0);
        OffsetDateTime expected = OffsetDateTime.of(1970, 1, 1, 12, 0, 0,0, ZoneOffset.UTC);

        OffsetDateTime actual = LessonMapper.toOffsetDateTime(localDateTime);
        assertEquals(expected, actual);

        assertNull(LessonMapper.toOffsetDateTime(null));
    }
}