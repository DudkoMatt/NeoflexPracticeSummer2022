package ru.dudkomv.neoflexpractice.lesson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.course.CourseService;
import ru.dudkomv.neoflexpractice.exception.CourseEntityNotFoundException;
import ru.dudkomv.neoflexpractice.exception.LessonEntityNotFoundException;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonCreationDto;
import ru.dudkomv.neoflexpractice.lesson.dto.LessonUpdateDto;
import ru.dudkomv.neoflexpractice.lesson.mapper.LessonMapper;
import ru.dudkomv.neoflexpractice.teacher.TeacherService;
import ru.dudkomv.neoflexpractice.teacher.dto.TeacherDto;
import ru.dudkomv.neoflexpractice.user.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    private LessonMapper lessonMapper;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        lessonService = new LessonService(lessonMapper, lessonRepository, userService, teacherService, courseService);
        lessonService = Mockito.spy(lessonService);
    }

    @Test
    void create_valid() {
        LessonCreationDto creationDto = Mockito.mock(LessonCreationDto.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);
        Lesson lesson = Mockito.mock(Lesson.class);

        Mockito.when(creationDto.acceptLessonMapper(lessonMapper)).thenReturn(lesson);
        Mockito.when(lessonRepository.save(lesson)).thenReturn(lesson);
        Mockito.when(lessonMapper.fromEntity(lesson)).thenReturn(updateDto);

        LessonUpdateDto actualUpdateDto = lessonService.create(creationDto);
        assertEquals(updateDto, actualUpdateDto);
    }

    @Test
    void create_courseNotExist() {
        LessonCreationDto creationDto = Mockito.mock(LessonCreationDto.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);
        Long courseId = 1L;

        Mockito.when(creationDto.getCourseId()).thenReturn(courseId);

        Exception expected = new CourseEntityNotFoundException(courseId);
        Mockito.doThrow(expected).when(courseService).validateExists(courseId);

        Exception actual = assertThrows(expected.getClass(), () -> lessonService.create(creationDto));
        assertEquals(expected, actual);
        assertEquals("Course entity not found with id=" + courseId, expected.getMessage());
    }

    @Test
    void getAllForUser_username() {
        String username = "SomeUsername";
        Long userId = 1L;
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);
        List<LessonUpdateDto> expectedList = List.of(updateDto);

        Mockito.when(userService.getIdByUsername(username)).thenReturn(userId);
        Mockito.doReturn(expectedList).when(lessonService).getAllForUser(userId);

        Mockito.verify(lessonService, Mockito.times(0)).getAllForUser(Mockito.anyLong());
        List<LessonUpdateDto> actualList =  lessonService.getAllForUser(username);

        assertEquals(expectedList, actualList);
        Mockito.verify(lessonService, Mockito.times(1)).getAllForUser(userId);
    }

    @Test
    void getAllForUser_userId() {
        Long teacherId = 1L;
        Long userId = 2L;
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        Lesson lesson = Mockito.mock(Lesson.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(teacherDto.getId()).thenReturn(teacherId);
        Mockito.when(teacherService.getByUserId(userId)).thenReturn(teacherDto);
        Mockito.when(lessonRepository.findLessonsByTeacherIdOrderByStartDateTimeAsc(teacherId)).thenReturn(List.of(lesson));
        Mockito.when(lessonMapper.fromEntity(lesson)).thenReturn(updateDto);

        List<LessonUpdateDto> expected = List.of(updateDto);
        List<LessonUpdateDto> actual = lessonService.getAllForUser(userId);
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        Lesson lesson = Mockito.mock(Lesson.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        Mockito.when(lessonMapper.fromEntity(lesson)).thenReturn(updateDto);

        List<LessonUpdateDto> expected = List.of(updateDto);
        List<LessonUpdateDto> actual = lessonService.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getById_null() {
        assertNull(lessonService.getById(null));
        Mockito.verify(lessonRepository, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void getById_nonNull_existing() {
        Long lessonId = 1L;
        Lesson lesson = Mockito.mock(Lesson.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(lessonMapper.fromEntity(lesson)).thenReturn(updateDto);
        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        LessonUpdateDto actual = lessonService.getById(lessonId);
        assertEquals(updateDto, actual);
    }

    @Test
    void getById_nonNull_nonExisting() {
        Long lessonId = 1L;

        Mockito.when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(LessonEntityNotFoundException.class, () -> lessonService.getById(lessonId));
        assertEquals("Lesson entity not found with id=" + lessonId, actual.getMessage());
    }

    @Test
    void update_existing() {
        Long lessonId = 1L;
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(updateDto.getId()).thenReturn(lessonId);
        Mockito.when(lessonRepository.existsById(lessonId)).thenReturn(true);
        Mockito.doReturn(updateDto).when(lessonService).create(updateDto);

        LessonUpdateDto actual = lessonService.update(updateDto);
        assertEquals(updateDto, actual);
        Mockito.verify(lessonService, Mockito.times(1)).create(updateDto);
    }

    @Test
    void update_nonExisting() {
        Long lessonId = 1L;
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(updateDto.getId()).thenReturn(lessonId);

        Exception actual = assertThrows(LessonEntityNotFoundException.class, () -> lessonService.update(updateDto));
        assertEquals("Lesson entity not found with id=" + lessonId, actual.getMessage());
    }

    @Test
    void deleteById_existing() {
        Long lessonId = 1L;

        Mockito.when(lessonRepository.existsById(lessonId)).thenReturn(true);
        assertDoesNotThrow(() -> lessonService.deleteById(lessonId));
        Mockito.verify(lessonRepository, Mockito.times(1)).deleteById(lessonId);
    }

    @Test
    void deleteById_nonExisting() {
        Long lessonId = 1L;

        Exception actual = assertThrows(LessonEntityNotFoundException.class, () -> lessonService.deleteById(lessonId));
        assertEquals("Lesson entity not found with id=" + lessonId, actual.getMessage());
    }

    @Test
    void getLessonsByCourseIdOrderByStartDateTimeAsc() {
        Long courseId = 1L;
        Lesson lesson = Mockito.mock(Lesson.class);
        LessonUpdateDto updateDto = Mockito.mock(LessonUpdateDto.class);

        Mockito.when(lessonRepository.findLessonsByCourseIdOrderByStartDateTimeAsc(courseId)).thenReturn(List.of(lesson));
        Mockito.when(lessonMapper.fromEntity(lesson)).thenReturn(updateDto);

        List<LessonUpdateDto> expected = List.of(updateDto);
        List<LessonUpdateDto> actual = lessonService.getLessonsByCourseIdOrderByStartDateTimeAsc(courseId);
        assertEquals(expected, actual);
    }

    @Test
    void cloneLessons() {
        Mockito.verify(lessonRepository, Mockito.times(0)).cloneLessons(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
        assertDoesNotThrow(() -> lessonService.cloneLessons(1L, 1L, 1L));
        Mockito.verify(lessonRepository, Mockito.times(1)).cloneLessons(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());
    }
}