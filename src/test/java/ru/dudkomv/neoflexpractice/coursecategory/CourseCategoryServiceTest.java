package ru.dudkomv.neoflexpractice.coursecategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;
import ru.dudkomv.neoflexpractice.coursecategory.mapper.CourseCategoryMapper;
import ru.dudkomv.neoflexpractice.exception.CourseCategoryEntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CourseCategoryServiceTest {
    @Mock
    private CourseCategoryMapper categoryMapper;

    @Mock
    private CourseCategoryRepository categoryRepository;

    private CourseCategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CourseCategoryService(categoryMapper, categoryRepository);
        categoryService = Mockito.spy(categoryService);
    }

    @Test
    void create() {
        CourseCategoryCreationDto courseCategoryCreationDto = Mockito.mock(CourseCategoryCreationDto.class);
        CourseCategory courseCategory = Mockito.mock(CourseCategory.class);

        Mockito.when(courseCategoryCreationDto.acceptCourseCategoryMapper(categoryMapper)).thenReturn(courseCategory);
        Mockito.when(categoryRepository.save(courseCategory)).thenReturn(courseCategory);

        CourseCategory actual = categoryService.create(courseCategoryCreationDto);
        assertEquals(courseCategory, actual);
    }

    @Test
    void getAll() {
        CourseCategory courseCategory = Mockito.mock(CourseCategory.class);
        List<CourseCategory> expected = List.of(courseCategory);

        Mockito.when(categoryRepository.findAll()).thenReturn(expected);

        List<CourseCategory> actual = categoryService.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getById_null() {
        assertNull(categoryService.getById(null));
        Mockito.verify(categoryRepository, Mockito.times(0)).findById(Mockito.anyLong());
    }

    @Test
    void getById_nonNull_existing() {
        Long categoryId = 1L;
        CourseCategory expected = Mockito.mock(CourseCategory.class);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expected));

        CourseCategory actual = categoryService.getById(categoryId);
        assertEquals(expected, actual);
    }

    @Test
    void getById_nonNull_nonExisting() {
        Long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception actual = assertThrows(CourseCategoryEntityNotFoundException.class, () -> categoryService.getById(categoryId));
        assertEquals("Course category entity not found with id=" + categoryId, actual.getMessage());
    }

    @Test
    void update_existing() {
        Long categoryId = 1L;
        CourseCategory expected = Mockito.mock(CourseCategory.class);
        CourseCategoryUpdateDto updateDto = Mockito.mock(CourseCategoryUpdateDto.class);
        Mockito.when(updateDto.getId()).thenReturn(categoryId);

        Mockito.doReturn(expected).when(categoryService).create(updateDto);
        Mockito.when(categoryRepository.existsById(categoryId)).thenReturn(true);

        CourseCategory actual = categoryService.update(updateDto);
        assertEquals(expected, actual);
        Mockito.verify(categoryService, Mockito.times(1)).create(updateDto);
    }

    @Test
    void update_nonExisting() {
        Long categoryId = 1L;

        Exception actual = assertThrows(CourseCategoryEntityNotFoundException.class, () -> categoryService.getById(categoryId));
        assertEquals("Course category entity not found with id=" + categoryId, actual.getMessage());
    }

    @Test
    void deleteById_existing() {
        Long categoryId = 1L;

        Mockito.when(categoryRepository.existsById(categoryId)).thenReturn(true);
        assertDoesNotThrow(() -> categoryService.deleteById(categoryId));
        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(categoryId);
    }

    @Test
    void deleteById_nonExisting() {
        Long categoryId = 1L;

        Exception actual = assertThrows(CourseCategoryEntityNotFoundException.class, () -> categoryService.deleteById(categoryId));
        assertEquals("Course category entity not found with id=" + categoryId, actual.getMessage());
    }

    @Test
    void validateExists() {
        Long categoryId = 1L;

        Exception actual = assertThrows(CourseCategoryEntityNotFoundException.class, () -> categoryService.validateExists(categoryId));
        assertEquals("Course category entity not found with id=" + categoryId, actual.getMessage());

        Mockito.when(categoryRepository.existsById(categoryId)).thenReturn(true);
        assertDoesNotThrow(() -> categoryService.validateExists(categoryId));
    }
}