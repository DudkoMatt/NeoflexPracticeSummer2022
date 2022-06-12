package ru.dudkomv.neoflexpractice.lesson;

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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("lessons")
// @StartIsBeforeEndConstraint  - checked in DTO
public class Lesson {
    @Id
    @Column("id")
    @Nullable
    private Long id;

    @Column("title")
    @NotBlank(message = "Lesson title must not be blank")
    @Length(max = 255, message = "Lesson title must be less or equal than 255 chars long")
    private String title;

    @Column("description")
    @NotBlank(message = "Lesson description must not be blank")
    private String description;

    @Column("teacher_id")
    @NotNull(message = "Teacher's id must not be null in teacher reference")
    private Long teacherId;

    @Column("course_id")
    @NotNull(message = "Course id must not be null in course reference")
    private Long courseId;

    @Column("start_datetime")
    @Nullable
    private LocalDateTime startDateTime;

    @Column("end_datetime")
    @Nullable
    private LocalDateTime endDateTime;
}
