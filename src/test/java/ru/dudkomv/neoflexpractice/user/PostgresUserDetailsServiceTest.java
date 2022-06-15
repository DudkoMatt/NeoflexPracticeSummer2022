package ru.dudkomv.neoflexpractice.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dudkomv.neoflexpractice.exception.UserDisabledException;
import ru.dudkomv.neoflexpractice.exception.UserEntityNotFoundException;
import ru.dudkomv.neoflexpractice.role.DefinedRoles;
import ru.dudkomv.neoflexpractice.role.Role;
import ru.dudkomv.neoflexpractice.role.RoleService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PostgresUserDetailsServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    private PostgresUserDetailsService postgresUserDetailsService;

    @BeforeEach
    void setUp() {
        postgresUserDetailsService = new PostgresUserDetailsService(userService, roleService);
        postgresUserDetailsService = Mockito.spy(postgresUserDetailsService);
    }

    @Test
    void loadUserByUsername_userExist_userEnabled() {
        String username = "SomeUsername";
        String password = "Password";
        User user = Mockito.mock(User.class);
        Long userId = 1L;
        Role role = Role.of(1L, DefinedRoles.ROLE_ADMIN);

        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(user.getEnabled()).thenReturn(true);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(roleService.getAllByUserId(userId)).thenReturn(List.of(role));

        Mockito.when(user.getUsername()).thenReturn(username);
        Mockito.when(user.getPassword()).thenReturn(password);

        List<SimpleGrantedAuthority> expectedAuthorities = List.of(new SimpleGrantedAuthority(role.getName()));
        var expected = new org.springframework.security.core.userdetails.User(username, password, expectedAuthorities);
        UserDetails actual = postgresUserDetailsService.loadUserByUsername(username);

        assertEquals(expected, actual);
    }

    @Test
    void loadUserByUsername_userExist_userDisabled() {
        String username = "SomeUsername";
        User user = Mockito.mock(User.class);

        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        Exception actual = assertThrows(UserDisabledException.class, () -> postgresUserDetailsService.loadUserByUsername(username));
        assertEquals("User with username='" + username + "' is disabled", actual.getMessage());
    }

    @Test
    void loadUserByUsername_userNotExist() {
        String username = "SomeUsername";

        Exception actual = assertThrows(UserEntityNotFoundException.class, () -> postgresUserDetailsService.loadUserByUsername(username));
        assertEquals("User entity not found with username=" + username, actual.getMessage());
    }
}