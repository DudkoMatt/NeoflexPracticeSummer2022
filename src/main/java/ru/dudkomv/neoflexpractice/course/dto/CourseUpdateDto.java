package ru.dudkomv.neoflexpractice.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dudkomv.neoflexpractice.course.Course;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Course update dto")
public class CourseUpdateDto extends CourseCreationDto {
    @NotNull(message = "Course id must not be null for updating")
    @Schema(description = "Id of the updating course", example = "0", required = true)
    private Long id;

    @Override
    public Course acceptCourseMapper(CourseMapper courseMapper) {
        return courseMapper.toEntity(this);
    }
}
