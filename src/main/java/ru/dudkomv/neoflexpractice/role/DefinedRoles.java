package ru.dudkomv.neoflexpractice.role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component("DefinedRoles")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefinedRoles {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
}
