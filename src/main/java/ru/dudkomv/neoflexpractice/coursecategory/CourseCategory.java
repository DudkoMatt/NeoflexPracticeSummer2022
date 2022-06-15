package ru.dudkomv.neoflexpractice.coursecategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("course_category")
public class CourseCategory {
    @Id
    @Column("id")
    @Nullable
    @Schema(description = "Id of the course category", example = "0", nullable = true)
    private Long id;

    @Column("title")
    @NotBlank(message = "Course category title must not be blank")
    @Length(max = 255, message = "Course category title must be less or equal than 255 chars long")
    @Schema(description = "Title of the course category", example = "Title")
    private String title;

    @Column("parent_id")
    @Nullable
    @Schema(description = "Course category parent reference by id", example = "0", nullable = true)
    private Long parentId;
}
