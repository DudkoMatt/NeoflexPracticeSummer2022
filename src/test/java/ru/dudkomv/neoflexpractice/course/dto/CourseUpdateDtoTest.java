package ru.dudkomv.neoflexpractice.course.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.course.Course;
import ru.dudkomv.neoflexpractice.course.mapper.CourseMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourseUpdateDtoTest {
    @Mock
    private CourseMapper courseMapper;

    @Test
    void acceptCourseMapper() {
        CourseUpdateDto updateDto = CourseUpdateDto.builder().build();
        Course expectedCourse = new Course();

        Mockito.when(courseMapper.toEntity(updateDto)).thenReturn(expectedCourse);

        Course actualCourse = updateDto.acceptCourseMapper(courseMapper);
        assertEquals(expectedCourse, actualCourse);
    }
}