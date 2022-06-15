package ru.dudkomv.neoflexpractice.teacher.security;

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
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherUpdateDto;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TeacherPermissionEvaluatorTest {
    private static final String MOCK_USERNAME = "SomeUsername";

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    private TeacherPermissionEvaluator teacherPermissionEvaluator;

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
        teacherPermissionEvaluator = new TeacherPermissionEvaluator(userService, teacherService);
        teacherPermissionEvaluator = Mockito.spy(teacherPermissionEvaluator);
    }

    @Test
    void canView_notAdmin_userNotMatch() {
        Long teacherId = 0L;
        Long userId = 1L;
        Long anotherUserId = 2L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(teacherService.getById(teacherId)).thenReturn(teacherDto);
        Mockito.when(teacherDto.getUserId()).thenReturn(userId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(anotherUserId));

        Boolean expectedAnswer = false;
        Boolean actualAnswer = teacherPermissionEvaluator.canView(teacherId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canView_notAdmin_userMatch() {
        Long teacherId = 2L;
        Long userId = 1L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(teacherService.getById(teacherId)).thenReturn(teacherDto);
        Mockito.when(teacherDto.getUserId()).thenReturn(userId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = true;
        Boolean actualAnswer = teacherPermissionEvaluator.canView(teacherId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void canView_admin() {
        Long teacherId = 1L;
        Long userId = 1L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);

        Mockito.when(teacherService.getById(teacherId)).thenReturn(teacherDto);
        Mockito.when(teacherDto.getUserId()).thenReturn(userId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Collection grantedAuthorityCollection = Set.of(new SimpleGrantedAuthority(DefinedRoles.ROLE_ADMIN));
        Mockito.when(userDetails.getAuthorities()).thenReturn(grantedAuthorityCollection);

        Boolean expectedAnswer = true;
        Boolean actualAnswer = teacherPermissionEvaluator.canView(teacherId, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canUpdate() {
        TeacherUpdateDto updateDto = Mockito.mock(TeacherUpdateDto.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;
        Long teacherId = 1L;

        Mockito.doReturn(expectedAnswer).when(teacherPermissionEvaluator).canView(teacherId, userDetails);
        Mockito.when(updateDto.getId()).thenReturn(teacherId);

        Boolean actualAnswer = teacherPermissionEvaluator.canUpdate(updateDto, userDetails);

        Mockito.verify(teacherPermissionEvaluator, Mockito.times(1)).canView(teacherId, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }
}