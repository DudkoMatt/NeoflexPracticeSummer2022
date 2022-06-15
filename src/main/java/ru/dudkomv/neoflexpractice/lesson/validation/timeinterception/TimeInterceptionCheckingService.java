package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.LessonRepository;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;

import java.time.LocalDateTime;
import java.util.function.Function;

import static ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper.toLocalDateTime;

@Component
@RequiredArgsConstructor
public class TimeInterceptionCheckingService {
    private final LessonRepository lessonRepository;

    @SuppressWarnings({"ConstantConditions", "java:S2259"})
    public boolean doesNotIntersect(Function<LessonRepository, Lesson> getFirstLessonWithEndTimeAfterNewLesson,
                                    LessonCreationDto creationDto) {
        Lesson foundLesson = getFirstLessonWithEndTimeAfterNewLesson.apply(lessonRepository);
        if (foundLesson == null) {
            return true;
        }

        return startTimeEqualsOrAfterPreviousEndTime(foundLesson.getStartDateTime(), toLocalDateTime(creationDto.getEndDateTime()));
    }

    private static boolean startTimeEqualsOrAfterPreviousEndTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.isEqual(endDateTime) || startDateTime.isAfter(endDateTime);
    }
}
