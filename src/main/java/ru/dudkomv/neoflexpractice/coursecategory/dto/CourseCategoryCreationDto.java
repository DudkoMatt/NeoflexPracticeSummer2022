package ru.dudkomv.neoflexpractice.coursecategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategory;
import ru.dudkomv.neoflexpractice.coursecategory.mapper.CourseCategoryMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@NoArgsConstructor
@Schema(description = "Course category creation dto")
public class CourseCategoryCreationDto {
    @NotBlank(message = "Course category title must not be blank")
    @Length(max = 255, message = "Course category title must be less or equal than 255 chars long")
    @Schema(description = "Title of the course category", example = "Title", required = true)
    private String title;

    @NotNull(message = "Course category parent id must not be null")
    @Schema(description = "Course category parent reference by id", example = "0", required = true)
    private Long parentId;

    public CourseCategory acceptCourseCategoryMapper(CourseCategoryMapper courseCategoryMapper) {
        return courseCategoryMapper.toEntity(this);
    }
}
