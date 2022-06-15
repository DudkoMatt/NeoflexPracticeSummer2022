package ru.dudkomv.neoflexpractice.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;
import ru.dudkomv.neoflexpractice.course.type.CourseType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("courses")
public class Course {
    @Id
    @Column("id")
    @Nullable
    private Long id;

    @Column("title")
    @NotBlank(message = "Course title must not be blank")
    @Length(max = 255, message = "Course title must be less or equal than 255 chars long")
    private String title;

    @Column("description")
    @NotBlank(message = "Course description must not be blank")
    private String description;

    @Column("category_id")
    @NotNull(message = "Course category must not be null")
    private Long categoryId;

    @Column("curator_id")
    @NotNull(message = "Course curator must not be null")
    private Long curatorId;

    @Column("student_count")
    @NotNull(message = "Course student count must not be null")
    @PositiveOrZero(message = "Course student count must be positive or zero")
    private Long studentCount;

    @Column("type")
    @NotNull(message = "Course type must not be null")
    private CourseType type;
}
