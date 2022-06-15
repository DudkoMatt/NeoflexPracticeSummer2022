package ru.dudkomv.neoflexpractice.teacher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.dudkomv.neoflexpractice.teacher.validation.birthday.IsBeforeTodayConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Teacher dto")
public class TeacherDto {
    @NotNull(message = "Teacher's id must not be null for updating")
    @Schema(description = "Id of the updating teacher", example = "0", required = true)
    private Long id;

    @NotNull(message = "Teacher's user id must not be null")
    @Schema(description = "User id of the teacher", example = "0", required = true)
    private Long userId;

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
}
