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
class CourseCategoryCreationDtoTest {
    @Mock
    private CourseCategoryMapper courseCategoryMapper;

    @Test
    void acceptCourseCategoryMapper() {
        CourseCategoryCreationDto creationDto = CourseCategoryCreationDto.builder().build();
        CourseCategory courseCategory = new CourseCategory();

        Mockito.when(courseCategoryMapper.toEntity(creationDto)).thenReturn(courseCategory);

        CourseCategory actualCourseCategory = creationDto.acceptCourseCategoryMapper(courseCategoryMapper);
        assertEquals(courseCategory, actualCourseCategory);
    }
}