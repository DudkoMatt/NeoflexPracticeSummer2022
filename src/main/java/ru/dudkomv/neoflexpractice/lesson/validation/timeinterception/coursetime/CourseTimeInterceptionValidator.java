package ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.coursetime;

import lombok.RequiredArgsConstructor;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.LessonRepository;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;
import ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.TimeInterceptionCheckingService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.function.Function;

import static ru.dudkomv.neoflexpractice.lesson.util.LessonUtils.startOrEndTimeIsNull;

@RequiredArgsConstructor
public class CourseTimeInterceptionValidator implements ConstraintValidator<CourseTimeInterceptionConstraint, LessonCreationDto> {
    private final TimeInterceptionCheckingService timeCheckingService;

    @Override
    public boolean isValid(LessonCreationDto creationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (startOrEndTimeIsNull(creationDto)) {
            return true;
        }

        return timeCheckingService
                .doesNotIntersect(
                        getFirstLessonByCourseIdAndEndTimeAfter(creationDto, LessonMapper.toLocalDateTime(creationDto.getStartDateTime())),
                        creationDto
                );
    }

    private static Function<LessonRepository, Lesson> getFirstLessonByCourseIdAndEndTimeAfter(LessonCreationDto creationDto, LocalDateTime afterDateTime) {
        return lessonRepository ->
                lessonRepository.findFirstByCourseIdAndEndDateTimeAfter(creationDto.getCourseId(), afterDateTime, creationDto.getLessonId());
    }
}
