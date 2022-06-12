package ru.dudkomv.neoflexpractice.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import ru.dudkomv.neoflexpractice.course.Course;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;
import ru.dudkomv.neoflexpractice.course.type.CourseType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@SuperBuilder
@NoArgsConstructor
@Schema(description = "Course creation dto")
public class CourseCreationDto {
    @NotBlank(message = "Course title must not be blank")
    @Length(max = 255, message = "Course title must be less or equal than 255 chars long")
    @Schema(description = "Title of the course", example = "Title", required = true)
    private String title;

    @NotBlank(message = "Course description must not be blank")
    @Schema(description = "Description of the course", example = "Description", required = true)
    private String description;

    @NotNull(message = "Course category must not be null")
    @Schema(description = "Course category reference by id", example = "1", required = true)
    private Long categoryId;

    @Nullable
    @Schema(description = "Course curator id (teacher id). If null - get current teacher id of the current logged in user", nullable = true, example = "null")
    private Long curatorId;

    @NotNull(message = "Course student count must not be null")
    @PositiveOrZero(message = "Course student count must be positive or zero")
    @Schema(description = "Count of the students enrolled", example = "100", required = true)
    private Long studentCount;

    @NotNull(message = "Course type must not be null")
    @Valid
    @Schema(description = "Type of the course", required = true)
    private CourseType type;

    public Course acceptCourseMapper(CourseMapper courseMapper) {
        return courseMapper.toEntity(this);
    }
}
