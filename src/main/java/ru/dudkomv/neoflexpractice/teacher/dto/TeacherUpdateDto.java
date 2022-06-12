package ru.dudkomv.neoflexpractice.teacher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.dudkomv.neoflexpractice.teacher.Teacher;
import ru.dudkomv.neoflexpractice.teacher.mapper.TeacherMapper;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "Teacher update dto")
public class TeacherUpdateDto extends TeacherCreationDto {
    @NotNull(message = "Teacher's id must not be null for updating")
    @Schema(description = "Id of the updating teacher", example = "0", required = true)
    private Long id;

    @Override
    public Teacher acceptTeacherMapper(TeacherMapper teacherMapper) {
        return teacherMapper.toEntity(this);
    }
}
