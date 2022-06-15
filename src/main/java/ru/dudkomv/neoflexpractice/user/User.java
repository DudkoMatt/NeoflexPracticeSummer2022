package ru.dudkomv.neoflexpractice.user;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table("users")
public class User {
    @Id
    @Column("id")
    @Nullable
    private Long id;

    @Column("username")
    @NotBlank(message = "User name must not be blank")
    @Length(max = 50, message = "User name must be less or equal than 50 chars long")
    private String username;

    @Column("password")
    @NotBlank(message = "User password must not be blank")
    @Length(min = 4, message = "User password must be greater than 4 characters")
    private String password;

    @Column("enabled")
    @NotNull(message = "User enabled property must not be null")
    @Builder.Default
    private Boolean enabled = true;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.enabled = true;
    }
}

