package ru.dudkomv.neoflexpractice.role;

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

import static ru.dudkomv.neoflexpractice.role.DefinedRoles.ROLE_TEACHER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("roles")
@Schema(description = "User role")
public class Role {
    @Id
    @Column("id")
    @Nullable
    @Schema(description = "Id of the role", example = "0", required = true)
    private Long id;

    @Column("name")
    @NotBlank(message = "User role name must not be blank")
    @Length(max = 50, message = "User role name must be less or equal than 50 chars long")
    @Builder.Default
    @Schema(description = "Name of the role", example = ROLE_TEACHER, required = true)
    private String name = ROLE_TEACHER;
}