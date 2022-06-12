package ru.dudkomv.neoflexpractice.teacher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.dudkomv.neoflexpractice.teacher.Teacher;
import ru.dudkomv.neoflexpractice.teacher.mapper.TeacherMapper;
import ru.dudkomv.neoflexpractice.teacher.validation.birthday.IsBeforeTodayConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@Schema(description = "Teacher creation dto")
public class TeacherCreationDto {
    @NotBlank(message = "User name must not be blank")
    @Length(max = 50, message = "User name must be less or equal than 50 chars long")
    @Schema(description = "Teacher's username", example = "SomeUsername", required = true)
    private String username;

    @NotBlank(message = "User password must not be blank")
    @Length(min = 4, message = "User password must be greater than 4 characters")
    @Schema(description = "Teacher's password", example = "SomePassword", required = true)
    private String password;

    @NotBlank(message = "Teacher's first name must not be blank")
    @Length(max = 255, message = "Teacher's first name must be less or equal than 255 chars long")
    @Schema(description = "Teacher's first name", example = "Name", required = true)
    private String firstName;

    @NotBlank(message = "Teacher's last name must not be blank")
    @Length(max = 255, message = "Teacher's last name must be less or equal than 255 chars long")
    @Schema(description = "Teacher's last name", example = "Surname", required = true)
    private String lastName;

    @NotNull(message = "Teacher's last name must not be null (use empty string instead)")
    @Length(max = 255, message = "Teacher's second name must be less or equal than 255 chars long")
    @Schema(description = "Teacher's second name", example = "Second name", required = true)
    private String secondName;

    @IsBeforeTodayConstraint(message = "Teacher's birthday must be in the past")
    @Schema(description = "Teacher's birthday", example = "2022-01-23", required = true)
    private LocalDate birthday;

    public Teacher acceptTeacherMapper(TeacherMapper teacherMapper) {
        return teacherMapper.toEntity(this);
    }
}
