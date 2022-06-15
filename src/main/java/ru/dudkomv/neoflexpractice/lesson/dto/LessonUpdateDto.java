package ru.dudkomv.neoflexpractice.lesson.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dudkomv.neoflexpractice.lesson.Lesson;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Lesson update dto")
public class LessonUpdateDto extends LessonCreationDto {
    @NotNull(message = "Lesson id must not be null for updating")
    @Schema(description = "Id of the updating lesson", example = "0", required = true)
    private Long id;

    @Override
    public Lesson acceptLessonMapper(LessonMapper lessonMapper) {
        return lessonMapper.toEntity(this);
    }

    @Override
    @JsonIgnore
    public Long getLessonId() {
        return id;
    }
}
