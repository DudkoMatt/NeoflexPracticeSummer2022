package ru.dudkomv.neoflexpractice.course.security;

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
import ru.dudkomv.neoflexpractice.course.CourseService;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.role.DefinedRoles;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CoursePermissionEvaluatorTest {
    private static final String MOCK_USERNAME = "SomeUsername";

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    private CoursePermissionEvaluator coursePermissionEvaluator;

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
        coursePermissionEvaluator = new CoursePermissionEvaluator(userService, teacherService, courseService);
        coursePermissionEvaluator = Mockito.spy(coursePermissionEvaluator);
    }

    @Test
    void canCreate_whenCuratorIdIsNotNull_notAdmin_userNotMatch() {
        Long curatorId = 3L;
        Long userId = 1L;
        Long anotherUserId = 2L;
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .curatorId(curatorId)
                .build();

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.getIdByTeacherId(curatorId)).thenReturn(userId);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(anotherUserId));

        Boolean expectedAnswer = false;
        Boolean actualAnswer = coursePermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canCreate_whenCuratorIdIsNotNull_notAdmin_userMatch() {
        Long curatorId = 1L;
        Long userId = 2L;
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .curatorId(curatorId)
                .build();

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.getIdByTeacherId(curatorId)).thenReturn(userId);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = true;
        Boolean actualAnswer = coursePermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void canCreate_whenCuratorIdIsNotNull_admin() {
        Long curatorId = 1L;
        Long userId = 2L;
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .curatorId(curatorId)
                .build();

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Collection grantedAuthorityCollection = Set.of(new SimpleGrantedAuthority(DefinedRoles.ROLE_ADMIN));
        Mockito.when(userDetails.getAuthorities()).thenReturn(grantedAuthorityCollection);

        Mockito.when(userService.getIdByTeacherId(curatorId)).thenReturn(userId);

        Boolean expectedAnswer = true;
        Boolean actualAnswer = coursePermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canCreate_whenCuratorIdIsNull() {
        Long userId = 1L;
        Long teacherId = 1L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        CourseCreationDto creationDto = CourseCreationDto.builder()
                .curatorId(null)
                .build();

        creationDto = Mockito.spy(creationDto);
        Mockito.verify(creationDto, Mockito.times(0)).setCuratorId(teacherId);
        assertNull(creationDto.getCuratorId());

        Mockito.when(userService.getIdByUsername(MOCK_USERNAME)).thenReturn(userId);
        Mockito.when(teacherService.getByUserId(userId)).thenReturn(teacherDto);
        Mockito.when(teacherDto.getId()).thenReturn(teacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Boolean expectedAnswer = true;
        Boolean actualAnswer = coursePermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
        Mockito.verify(creationDto, Mockito.times(1)).setCuratorId(teacherId);
        assertEquals(teacherId, creationDto.getCuratorId());
        Mockito.verify(userService, Mockito.times(0)).getIdByTeacherId(Mockito.anyLong());
    }

    @Test
    void canView() {
        Long courseId = 1L;
        CourseUpdateDto updateDto = Mockito.mock(CourseUpdateDto.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.when(courseService.getById(courseId)).thenReturn(updateDto);
        Mockito.doReturn(expectedAnswer).when(coursePermissionEvaluator).canCreate(updateDto, userDetails);

        Boolean actualAnswer = coursePermissionEvaluator.canView(courseId, userDetails);

        Mockito.verify(coursePermissionEvaluator, Mockito.times(1)).canCreate(updateDto, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canUpdate() {
        CourseUpdateDto updateDto = Mockito.mock(CourseUpdateDto.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.doReturn(expectedAnswer).when(coursePermissionEvaluator).canCreate(updateDto, userDetails);

        Boolean actualAnswer = coursePermissionEvaluator.canUpdate(updateDto, userDetails);

        Mockito.verify(coursePermissionEvaluator, Mockito.times(1)).canCreate(updateDto, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canDelete() {
        Long courseId = 1L;
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.doReturn(expectedAnswer).when(coursePermissionEvaluator).canView(courseId, userDetails);

        Boolean actualAnswer = coursePermissionEvaluator.canDelete(courseId, userDetails);

        Mockito.verify(coursePermissionEvaluator, Mockito.times(1)).canView(courseId, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }
}