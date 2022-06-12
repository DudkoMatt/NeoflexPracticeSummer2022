package ru.dudkomv.neoflexpractice.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;
import ru.dudkomv.neoflexpractice.teacher.validation.birthday.IsBeforeTodayConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("teachers")
public class Teacher {
    @Id
    @Column("id")
    @Nullable
    private Long id;

    @Column("user_id")
    @NotNull(message = "Teacher's user id must not be null")
    private Long userId;

    @Column("first_name")
    @NotBlank(message = "Teacher's first name must not be blank")
    @Length(max = 255, message = "Teacher's first name must be less or equal than 255 chars long")
    private String firstName;

    @Column("last_name")
    @NotBlank(message = "Teacher's last name must not be blank")
    @Length(max = 255, message = "Teacher's last name must be less or equal than 255 chars long")
    private String lastName;

    @Column("second_name")
    @NotNull(message = "Teacher's last name must not be null (use empty string instead)")
    @Length(max = 255, message = "Teacher's second name must be less or equal than 255 chars long")
    private String secondName;

    @Column("birthday")
    @IsBeforeTodayConstraint(message = "Teacher's birthday must be in the past")
    private LocalDate birthday;
}
