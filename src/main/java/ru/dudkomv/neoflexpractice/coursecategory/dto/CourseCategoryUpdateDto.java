package ru.dudkomv.neoflexpractice.coursecategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategory;
import ru.dudkomv.neoflexpractice.coursecategory.mapper.CourseCategoryMapper;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Course category update dto")
public class CourseCategoryUpdateDto extends CourseCategoryCreationDto {
    @NotNull(message = "Course category id must not be null for updating")
    @Schema(description = "Id of the updating course category", example = "0", required = true)
    private Long id;

    @Override
    public CourseCategory acceptCourseCategoryMapper(CourseCategoryMapper courseCategoryMapper) {
        return courseCategoryMapper.toEntity(this);
    }
}
