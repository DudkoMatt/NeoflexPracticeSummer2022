package ru.dudkomv.neoflexpractice.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.course.dto.CourseCreationDto;
import ru.dudkomv.neoflexpractice.course.dto.CourseUpdateDto;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategoryService;
import ru.dudkomv.neoflexpractice.exception.CourseCategoryEntityNotFoundException;
import ru.dudkomv.neoflexpractice.exception.CourseEntityNotFoundException;
import ru.dudkomv.neoflexpractice.exception.TeacherEntityNotFoundException;
import ru.dudkomv.neoflexpractice.lesson.LessonService;
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
class CourseServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseCategoryService courseCategoryService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private LessonService lessonService;

    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(userService, courseMapper, courseRepository, teacherService, lessonService, courseCategoryService);
        courseService = Mockito.spy(courseService);
    }

    @Test
    void create() {
        CourseCreationDto courseCreationDto = Mockito.mock(CourseCreationDto.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        Course course = Mockito.mock(Course.class);

        Mockito.when(courseCreationDto.acceptCourseMapper(courseMapper)).thenReturn(course);
        Mockito.when(courseRepository.save(course)).thenReturn(course);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        CourseUpdateDto actual = courseService.create(courseCreationDto);
        assertEquals(courseUpdateDto, actual);
    }

    @Test
    void getAll() {
        Course course = Mockito.mock(Course.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        List<Course> courseList = List.of(course);

        Mockito.when(courseRepository.findAll()).thenReturn(courseList);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        List<CourseUpdateDto> expected = List.of(courseUpdateDto);
        List<CourseUpdateDto> actual = courseService.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void getAllForUser() {
        Long userId = 1L;
        Long teacherId = 2L;
        String username = "someUsername";
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        Course course = Mockito.mock(Course.class);
        List<Course> courseList = List.of(course);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        Mockito.when(userService.getIdByUsername(username)).thenReturn(userId);
        Mockito.when(teacherDto.getId()).thenReturn(teacherId);
        Mockito.when(teacherService.getByUserId(userId)).thenReturn(teacherDto);
        Mockito.when(courseRepository.findCoursesByCuratorId(teacherId)).thenReturn(courseList);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        List<CourseUpdateDto> expected = List.of(courseUpdateDto);
        List<CourseUpdateDto> actual = courseService.getAllForUser(username);

        assertEquals(expected, actual);
    }

    @Test
    void getById_null() {
        assertNull(courseService.getById(null));
        Mockito.verify(courseRepository, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void getById_nonNull_existing() {
        Long courseId = 1L;
        Course course = Mockito.mock(Course.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        CourseUpdateDto actual = courseService.getById(courseId);
        assertEquals(courseUpdateDto, actual);
    }

    @Test
    void getById_nonNull_nonExisting() {
        Long courseId = 1L;

        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(CourseEntityNotFoundException.class, () -> courseService.getById(courseId));
        assertEquals("Course entity not found with id=" + courseId, actual.getMessage());
    }

    @Test
    void update_existing() {
        Long courseId = 1L;
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        Mockito.when(courseUpdateDto.getId()).thenReturn(courseId);
        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        Mockito.doReturn(courseUpdateDto).when(courseService).create(courseUpdateDto);

        CourseUpdateDto actual = courseService.update(courseUpdateDto);
        assertEquals(courseUpdateDto, actual);
        Mockito.verify(courseService, Mockito.times(1)).create(courseUpdateDto);
    }

    @Test
    void update_nonExisting() {
        Long courseId = 1L;
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        Mockito.when(courseUpdateDto.getId()).thenReturn(courseId);

        Exception actual = assertThrows(CourseEntityNotFoundException.class, () -> courseService.update(courseUpdateDto));
        assertEquals("Course entity not found with id=" + courseId, actual.getMessage());
    }

    @Test
    void deleteById_existing() {
        Long courseId = 1L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        assertDoesNotThrow(() -> courseService.deleteById(courseId));
        Mockito.verify(courseRepository, Mockito.times(1)).deleteById(courseId);
    }

    @Test
    void deleteById_nonExisting() {
        Long courseId = 1L;

        Exception actual = assertThrows(CourseEntityNotFoundException.class, () -> courseService.deleteById(courseId));
        assertEquals("Course entity not found with id=" + courseId, actual.getMessage());
    }

    @Test
    void getCoursesByCategoryIdRecursively_categoryExist() {
        Course course = Mockito.mock(Course.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        List<Course> courseList = List.of(course);
        Long categoryId = 1L;

        Mockito.when(courseRepository.findCoursesByCategoryIdRecursively(categoryId)).thenReturn(courseList);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        List<CourseUpdateDto> expected = List.of(courseUpdateDto);
        List<CourseUpdateDto> actual = courseService.getCoursesByCategoryIdRecursively(categoryId);

        assertEquals(expected, actual);
    }

    @Test
    void getCoursesByCategoryIdRecursively_categoryNotExist() {
        Long categoryId = 1L;

        Exception expected = new CourseCategoryEntityNotFoundException(categoryId);
        Mockito.doThrow(expected).when(courseCategoryService).validateExists(categoryId);

        Exception actual = assertThrows(expected.getClass(), () -> courseService.getCoursesByCategoryIdRecursively(categoryId));
        assertEquals(expected, actual);
        assertEquals("Course category entity not found with id=" + categoryId, expected.getMessage());
    }

    @Test
    void getCoursesByType() {
        Course course = Mockito.mock(Course.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        List<Course> courseList = List.of(course);
        String courseType = "courseType";

        Mockito.when(courseRepository.findCoursesByType(courseType)).thenReturn(courseList);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        List<CourseUpdateDto> expected = List.of(courseUpdateDto);
        List<CourseUpdateDto> actual = courseService.getCoursesByType(courseType);

        assertEquals(expected, actual);
    }

    @Test
    void getCoursesByCuratorId_curatorExist() {
        Course course = Mockito.mock(Course.class);
        CourseUpdateDto courseUpdateDto = Mockito.mock(CourseUpdateDto.class);
        List<Course> courseList = List.of(course);
        Long curatorId = 1L;

        Mockito.when(courseRepository.findCoursesByCuratorId(curatorId)).thenReturn(courseList);
        Mockito.when(courseMapper.fromEntity(course)).thenReturn(courseUpdateDto);

        List<CourseUpdateDto> expected = List.of(courseUpdateDto);
        List<CourseUpdateDto> actual = courseService.getCoursesByCuratorId(curatorId);

        assertEquals(expected, actual);
    }

    @Test
    void getCoursesByCuratorId_curatorNotExist() {
        Long curatorId = 1L;

        Exception expected = new TeacherEntityNotFoundException(curatorId);
        Mockito.doThrow(expected).when(teacherService).validateExists(curatorId);

        Exception actual = assertThrows(expected.getClass(), () -> courseService.getCoursesByCuratorId(curatorId));
        assertEquals(expected, actual);
        assertEquals("Teacher entity not found with id=" + curatorId, expected.getMessage());
    }

    @Test
    void cloneCourse_existing() {
        Long courseId = 1L;
        Long newCourseId = 4L;
        Long userId = 2L;
        Long teacherId = 3L;
        String username = "someUsername";
        TeacherDto teacherDto = Mockito.mock(TeacherDto.class);
        CourseUpdateDto updateDto = Mockito.mock(CourseUpdateDto.class);
        CourseCreationDto creationDto = Mockito.mock(CourseCreationDto.class);
        CourseUpdateDto clonedCourseUpdateDto = Mockito.mock(CourseUpdateDto.class);

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        Mockito.when(userService.getIdByUsername(username)).thenReturn(userId);
        Mockito.when(teacherDto.getId()).thenReturn(teacherId);
        Mockito.when(teacherService.getByUserId(userId)).thenReturn(teacherDto);

        Mockito.doReturn(updateDto).when(courseService).getById(courseId);
        Mockito.when(courseMapper.toCreation(updateDto)).thenReturn(creationDto);

        Mockito.doReturn(clonedCourseUpdateDto).when(courseService).create(creationDto);
        Mockito.when(clonedCourseUpdateDto.getId()).thenReturn(newCourseId);

        CourseUpdateDto actual = courseService.cloneCourse(courseId, username);

        Mockito.verify(lessonService).cloneLessons(teacherId, courseId, newCourseId);
        assertEquals(clonedCourseUpdateDto, actual);
    }

    @Test
    void cloneCourse_nonExisting() {
        Long courseId = 1L;
        String username = "someUsername";

        Exception actual = assertThrows(CourseEntityNotFoundException.class, () -> courseService.cloneCourse(courseId, username));
        assertEquals("Course entity not found with id=" + courseId, actual.getMessage());
    }

    @Test
    void validateExists() {
        Long courseId = 1L;

        Exception actual = assertThrows(CourseEntityNotFoundException.class, () -> courseService.validateExists(courseId));
        assertEquals("Course entity not found with id=" + courseId, actual.getMessage());

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);
        assertDoesNotThrow(() -> courseService.validateExists(courseId));
    }
}