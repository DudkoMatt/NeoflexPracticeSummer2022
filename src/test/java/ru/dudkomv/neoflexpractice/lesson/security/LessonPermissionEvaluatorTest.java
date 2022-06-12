package ru.dudkomv.neoflexpractice.lesson.security;

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
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.lesson.LessonService;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;
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
class LessonPermissionEvaluatorTest {
    private static final String MOCK_USERNAME = "SomeUsername";

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;
    
    @Mock
    private LessonService lessonService;

    @Mock
    private CourseService courseService;
    
    private LessonPermissionEvaluator lessonPermissionEvaluator;

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
        lessonPermissionEvaluator = new LessonPermissionEvaluator(userService, teacherService, lessonService, courseService);
        lessonPermissionEvaluator = Mockito.spy(lessonPermissionEvaluator);
    }

    @Test
    void canCreate_whenCuratorIdIsNotNull_notAdmin_userMatch_courseNotMatch() {
        Long teacherId = 3L;
        Long anotherTeacherId = 4L;
        Long userId = 1L;
        Long anotherUserId = 2L;
        Long courseId = 5L;
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .teacherId(teacherId)
                .courseId(courseId)
                .build();

        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        Mockito.when(courseService.getById(courseId)).thenReturn(courseUpdateDto);
        Mockito.when(courseUpdateDto.getCuratorId()).thenReturn(anotherTeacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.getIdByTeacherId(teacherId)).thenReturn(userId);
        Mockito.when(userService.getIdByTeacherId(anotherTeacherId)).thenReturn(anotherUserId);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = false;
        Boolean actualAnswer = lessonPermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canCreate_whenCuratorIdIsNotNull_notAdmin_userNotMatch() {
        Long teacherId = 3L;
        Long userId = 1L;
        Long anotherUserId = 2L;
        Long courseId = 5L;
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .teacherId(teacherId)
                .courseId(courseId)
                .build();

        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        Mockito.when(courseService.getById(courseId)).thenReturn(courseUpdateDto);
        Mockito.when(courseUpdateDto.getCuratorId()).thenReturn(teacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.getIdByTeacherId(teacherId)).thenReturn(userId);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(anotherUserId));

        Boolean expectedAnswer = false;
        Boolean actualAnswer = lessonPermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canCreate_whenCuratorIdIsNotNull_notAdmin_userMatch_courseMatch() {
        Long teacherId = 1L;
        Long userId = 2L;
        Long courseId = 3L;
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .teacherId(teacherId)
                .courseId(courseId)
                .build();

        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        Mockito.when(courseService.getById(courseId)).thenReturn(courseUpdateDto);
        Mockito.when(courseUpdateDto.getCuratorId()).thenReturn(teacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Collections.emptySet());
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);

        Mockito.when(userService.getIdByTeacherId(teacherId)).thenReturn(userId);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = true;
        Boolean actualAnswer = lessonPermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void canCreate_whenCuratorIdIsNotNull_admin() {
        Long teacherId = 1L;
        Long userId = 2L;
        Long courseId = 3L;
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .teacherId(teacherId)
                .courseId(courseId)
                .build();

        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        Mockito.when(courseService.getById(courseId)).thenReturn(courseUpdateDto);
        Mockito.when(courseUpdateDto.getCuratorId()).thenReturn(teacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Collection grantedAuthorityCollection = Set.of(new SimpleGrantedAuthority(DefinedRoles.ROLE_ADMIN));
        Mockito.when(userDetails.getAuthorities()).thenReturn(grantedAuthorityCollection);

        Mockito.when(userService.getIdByTeacherId(teacherId)).thenReturn(userId);

        Boolean expectedAnswer = true;
        Boolean actualAnswer = lessonPermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canCreate_whenCuratorIdIsNull() {
        Long userId = 1L;
        Long teacherId = 1L;
        Long courseId = 1L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        LessonCreationDto creationDto = LessonCreationDto.builder()
                .teacherId(null)
                .courseId(courseId)
                .build();
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        creationDto = Mockito.spy(creationDto);
        Mockito.verify(creationDto, Mockito.times(0)).setTeacherId(teacherId);
        assertNull(creationDto.getTeacherId());

        Mockito.when(userService.getIdByUsername(MOCK_USERNAME)).thenReturn(userId);
        Mockito.when(teacherService.getByUserId(userId)).thenReturn(teacherDto);

        Mockito.when(userService.getIdByTeacherId(teacherId)).thenReturn(userId);
        Mockito.when(courseService.getById(courseId)).thenReturn(courseUpdateDto);
        Mockito.when(courseUpdateDto.getCuratorId()).thenReturn(teacherId);
        Mockito.when(teacherDto.getId()).thenReturn(teacherId);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(MOCK_USERNAME);
        Mockito.when(userService.findIdByUsername(MOCK_USERNAME)).thenReturn(Optional.of(userId));

        Boolean expectedAnswer = true;
        Boolean actualAnswer = lessonPermissionEvaluator.canCreate(creationDto, userDetails);

        assertEquals(expectedAnswer, actualAnswer);
        Mockito.verify(creationDto, Mockito.times(1)).setTeacherId(teacherId);
        assertEquals(teacherId, creationDto.getTeacherId());
        Mockito.verify(userService, Mockito.times(2)).getIdByTeacherId(Mockito.anyLong());
    }

    @Test
    void canView() {
        Long lessonId = 1L;
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.when(lessonService.getById(lessonId)).thenReturn(updateDto);
        Mockito.doReturn(expectedAnswer).when(lessonPermissionEvaluator).canCreate(updateDto, userDetails);

        Boolean actualAnswer = lessonPermissionEvaluator.canView(lessonId, userDetails);

        Mockito.verify(lessonPermissionEvaluator, Mockito.times(1)).canCreate(updateDto, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canUpdate() {
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.doReturn(expectedAnswer).when(lessonPermissionEvaluator).canCreate(updateDto, userDetails);

        Boolean actualAnswer = lessonPermissionEvaluator.canUpdate(updateDto, userDetails);

        Mockito.verify(lessonPermissionEvaluator, Mockito.times(1)).canCreate(updateDto, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    void canDelete() {
        Long lessonId = 1L;
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Boolean expectedAnswer = true;

        Mockito.doReturn(expectedAnswer).when(lessonPermissionEvaluator).canView(lessonId, userDetails);

        Boolean actualAnswer = lessonPermissionEvaluator.canDelete(lessonId, userDetails);

        Mockito.verify(lessonPermissionEvaluator, Mockito.times(1)).canView(lessonId, userDetails);
        assertEquals(expectedAnswer, actualAnswer);
    }
}