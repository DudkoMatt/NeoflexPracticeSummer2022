package ru.dudkomv.neoflexpractice.lesson.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "teacherId", source = "teacherId")
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(target = "startDateTime", expression = "java(LessonMapper.toLocalDateTime(creationDto.getStartDateTime()))")
    @Mapping(target = "endDateTime", expression = "java(LessonMapper.toLocalDateTime(creationDto.getEndDateTime()))")
    Lesson toEntity(LessonCreationDto creationDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "teacherId", source = "teacherId")
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(target = "startDateTime", expression = "java(LessonMapper.toLocalDateTime(updateDto.getStartDateTime()))")
    @Mapping(target = "endDateTime", expression = "java(LessonMapper.toLocalDateTime(updateDto.getEndDateTime()))")
    Lesson toEntity(LessonUpdateDto updateDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "teacherId", source = "teacherId")
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(target = "startDateTime", expression = "java(LessonMapper.toOffsetDateTime(lesson.getStartDateTime()))")
    @Mapping(target = "endDateTime", expression = "java(LessonMapper.toOffsetDateTime(lesson.getEndDateTime()))")
    LessonUpdateDto fromEntity(Lesson lesson);

    static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
    }
}
