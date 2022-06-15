package ru.dudkomv.neoflexpractice.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService(roleRepository);
    }

    @Test
    void registerUser() {
        Long userId = 1L;
        User user = Mockito.mock(User.class);
        Long teacherRoleId = 2L;
        Role teacherRole = Mockito.mock(Role.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(teacherRole.getId()).thenReturn(teacherRoleId);
        Mockito.when(roleRepository.findByName(DefinedRoles.ROLE_TEACHER)).thenReturn(teacherRole);

        assertDoesNotThrow(() -> roleService.registerUser(user));
        Mockito.verify(roleRepository, Mockito.times(1)).setUserRole(userId, teacherRoleId);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        Role expectedRole = Role.of(1L, DefinedRoles.ROLE_TEACHER);
        List<Role> expectedRoleList = List.of(expectedRole);

        Mockito.when(roleRepository.findAllByUserId(userId)).thenReturn(expectedRoleList);

        List<Role> actualRoleList = roleService.getAllByUserId(userId);
        assertEquals(expectedRoleList, actualRoleList);
    }
}