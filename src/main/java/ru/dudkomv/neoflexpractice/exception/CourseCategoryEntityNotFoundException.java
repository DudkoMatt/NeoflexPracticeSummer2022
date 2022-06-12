package ru.dudkomv.neoflexpractice.exception;

public class CourseCategoryEntityNotFoundException extends EntityNotFoundException {
    public CourseCategoryEntityNotFoundException(Long courseCategoryId) {
        super("Course category entity not found with id=" + courseCategoryId);
    }
}