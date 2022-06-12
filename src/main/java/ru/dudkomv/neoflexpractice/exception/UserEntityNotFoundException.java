package ru.dudkomv.neoflexpractice.exception;

public class UserEntityNotFoundException extends EntityNotFoundException {
    public UserEntityNotFoundException(String username) {
        super("User entity not found with username=" + username);
    }

    public UserEntityNotFoundException(Long id) {
        super("User entity not found with id=" + id);
    }
}