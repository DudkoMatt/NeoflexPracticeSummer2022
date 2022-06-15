package ru.dudkomv.neoflexpractice.coursecategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryCreationDto;
import ru.dudkomv.neoflexpractice.coursecategory.dto.CourseCategoryUpdateDto;
import ru.dudkomv.neoflexpractice.coursecategory.mapper.CourseCategoryMapper;
import ru.dudkomv.neoflexpractice.exception.CourseCategoryEntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCategoryService {
    private final CourseCategoryMapper categoryMapper;
    private final CourseCategoryRepository categoryRepository;

    public CourseCategory create(CourseCategoryCreationDto creationDto) {
        return categoryRepository.save(
                creationDto.acceptCourseCategoryMapper(categoryMapper)
        );
    }

    public List<CourseCategory> getAll() {
        return categoryRepository.findAll();
    }

    public CourseCategory getById(Long id) {
        if (id == null) {
            return null;
        }

        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new CourseCategoryEntityNotFoundException(id));
    }

    @Transactional
    public CourseCategory update(CourseCategoryUpdateDto updateDto) {
        validateExists(updateDto.getId());
        return create(updateDto);
    }

    @Transactional
    public void deleteById(Long id) {
        validateExists(id);
        categoryRepository.deleteById(id);
    }

    public void validateExists(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CourseCategoryEntityNotFoundException(id);
        }
    }
}
