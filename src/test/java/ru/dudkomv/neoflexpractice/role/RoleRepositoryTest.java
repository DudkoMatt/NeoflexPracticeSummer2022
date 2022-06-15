package ru.dudkomv.neoflexpractice.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.dudkomv.neoflexpractice.ApplicationIntegrationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleRepositoryTest extends ApplicationIntegrationTest {
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void tearDown() {
        cleanAndMigrate();
    }

    @Test
    void findAllByUserId() {
        Long adminId = 0L;
        Long userId = 1L;

        List<Role> adminRoles = List.of(Role.of(0L, DefinedRoles.ROLE_ADMIN));
        List<Role> userRoles = List.of(Role.of(1L, DefinedRoles.ROLE_TEACHER));

        List<Role> actualAdminRoles = roleRepository.findAllByUserId(adminId);
        List<Role> actualUserRoles = roleRepository.findAllByUserId(userId);

        assertEquals(adminRoles, actualAdminRoles);
        assertEquals(userRoles, actualUserRoles);
    }

    @Test
    @Sql(statements = """
            truncate table users_roles restart identity cascade;
            truncate table users restart identity cascade;
            
            insert into users(id, username, password, enabled)
                values (0, 'admin', '$2a$12$c48Fc.pfTfcCiLQdo7DNEuOXMpmm35GbzbKFjUeFOeXuK18zesvAa', true);
            
            insert into users(id, username, password, enabled)
                values (1, 'user', '$2a$12$DC6JhVUEHWVjmclCHIod8.KWGRpNdDbSMhYSlQT1d.E2WsldvaxpO', true);
            """)
    void setUserRole() {
        Role adminRole = roleRepository.findByName(DefinedRoles.ROLE_ADMIN);
        Role teacherRole = roleRepository.findByName(DefinedRoles.ROLE_TEACHER);

        roleRepository.setUserRole(0L, adminRole.getId());
        roleRepository.setUserRole(1L, teacherRole.getId());

        List<Role> actualAdminRoles = roleRepository.findAllByUserId(0L);
        List<Role> actualUserRoles = roleRepository.findAllByUserId(1L);

        assertEquals(List.of(adminRole), actualAdminRoles);
        assertEquals(List.of(teacherRole), actualUserRoles);
    }
}