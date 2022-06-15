package ru.dudkomv.neoflexpractice.coursecategory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseCategoryRepository extends CrudRepository<CourseCategory, Long> {
    @Override
    List<CourseCategory> findAll();
}
