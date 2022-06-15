package ru.dudkomv.neoflexpractice.role.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dudkomv.neoflexpractice.role.DefinedRoles;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RolePermissionEvaluatorTest {
    private static final String MOCK_USERNAME = "SomeUsername";

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    private RolePermissionEvaluator rolePermissionEvaluator;

    @BeforeAll
    static void beforeAll() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(MOCK_USERNAME);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    void setUp() {
        rolePermissionEvaluator = new RolePermissionEvaluator(userService, teacherService);
        rolePermissionEvaluator = Mockito.spy(rolePermissionEvaluator);
    }

    @Test
    void canView_notAdmin_userNotMatch() {
        Long requestedUserId = 1L;
        Long userId = 2L;

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = false;
        Boolean actualAnswer = rolePermissionEvaluator.canView(requestedUserId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canView_notAdmin_userMatch() {
        Long requestedUserId = 1L;
        Long userId = 1L;

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = true;
        Boolean actualAnswer = rolePermissionEvaluator.canView(requestedUserId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void canView_admin() {
        Long requestedUserId = 1L;

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Collection grantedAuthorityCollection = Set.of(new SimpleGrantedAuthority(DefinedRoles.ROLE_ADMIN));
        Mockito.when(userDetails.getAuthorities()).thenReturn(grantedAuthorityCollection);

        Boolean expectedAnswer = true;
        Boolean actualAnswer = rolePermissionEvaluator.canView(requestedUserId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }
}