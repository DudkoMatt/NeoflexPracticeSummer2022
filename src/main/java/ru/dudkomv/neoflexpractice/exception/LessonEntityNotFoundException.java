package ru.dudkomv.neoflexpractice.exception;

public class LessonEntityNotFoundException extends EntityNotFoundException {
    public LessonEntityNotFoundException(Long lessonId) {
        super("Lesson entity not found with id=" + lessonId);
    }
}