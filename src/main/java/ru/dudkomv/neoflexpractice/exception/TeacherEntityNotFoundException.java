package ru.dudkomv.neoflexpractice.exception;

public class TeacherEntityNotFoundException extends EntityNotFoundException {
    public TeacherEntityNotFoundException(Long teacherId) {
        super("Teacher entity not found with id=" + teacherId);
    }
}