package ru.dudkomv.neoflexpractice.coursecategory.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.coursecategory.CourseCategory;
import ru.dudkomv.neoflexpractice.coursecategory.mapper.CourseCategoryMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourseCategoryUpdateDtoTest {
    @Mock
    private CourseCategoryMapper courseCategoryMapper;

    @Test
    void acceptCourseCategoryMapper() {
        CourseCategoryUpdateDto updateDto = CourseCategoryUpdateDto.builder().build();
        CourseCategory courseCategory = new CourseCategory();

        Mockito.when(courseCategoryMapper.toEntity(updateDto)).thenReturn(courseCategory);

        CourseCategory actualCourseCategory = updateDto.acceptCourseCategoryMapper(courseCategoryMapper);
        assertEquals(courseCategory, actualCourseCategory);
    }
}