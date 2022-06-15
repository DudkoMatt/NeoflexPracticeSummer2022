package ru.dudkomv.neoflexpractice.lesson.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;
import ru.dudkomv.neoflexpractice.lesson.validation.startendtime.StartIsBeforeEndConstraint;
import ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.coursetime.CourseTimeInterceptionConstraint;
import ru.dudkomv.neoflexpractice.lesson.validation.timeinterception.teachertime.TeacherTimeInterceptionConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@StartIsBeforeEndConstraint
@CourseTimeInterceptionConstraint
@TeacherTimeInterceptionConstraint
@Schema(description = "Lesson creation dto")
public class LessonCreationDto {
    @NotBlank(message = "Lesson title must not be blank")
    @Length(max = 255, message = "Lesson title must be less or equal than 255 chars long")
    @Schema(description = "Title of the lesson", example = "Title", required = true)
    private String title;

    @NotBlank(message = "Lesson description must not be blank")
    @Schema(description = "Description of the lesson", example = "Description", required = true)
    private String description;

    @Nullable
    @Schema(description = "Teacher id. If null - get current teacher id of the current logged in user", example = "null", nullable = true)
    private Long teacherId;

    @NotNull(message = "Lesson must have a course id")
    @Schema(description = "Course reference by id", example = "1", required = true)
    private Long courseId;

    @Nullable
    @Schema(description = "Start date time of the lesson", example = "2022-05-23T12:00:00.000Z", nullable = true)
    private OffsetDateTime startDateTime;

    @Nullable
    @Schema(description = "End date time of the lesson", example = "2022-05-23T13:00:00.000Z", nullable = true)
    private OffsetDateTime endDateTime;

    public Lesson acceptLessonMapper(LessonMapper lessonMapper) {
        return lessonMapper.toEntity(this);
    }

    @JsonIgnore
    public Long getLessonId() {
        return null;
    }
}
