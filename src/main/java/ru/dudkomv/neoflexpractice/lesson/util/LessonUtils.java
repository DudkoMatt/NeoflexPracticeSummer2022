package ru.dudkomv.neoflexpractice.lesson.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonUtils {
    public static boolean startOrEndTimeIsNull(LessonCreationDto creationDto) {
        return creationDto.getStartDateTime() == null || creationDto.getEndDateTime() == null;
    }

    public static boolean startAndEndTimeIsNotNull(LessonCreationDto creationDto) {
        return !startOrEndTimeIsNull(creationDto);
    }
}
