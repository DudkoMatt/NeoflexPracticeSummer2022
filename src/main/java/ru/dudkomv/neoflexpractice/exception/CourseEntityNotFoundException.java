package ru.dudkomv.neoflexpractice.exception;

public class CourseEntityNotFoundException extends EntityNotFoundException {
    public CourseEntityNotFoundException(Long courseId) {
        super("Course entity not found with id=" + courseId);
    }
}