package ru.dudkomv.neoflexpractice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(value = FORBIDDEN)
public class UserDisabledException extends RuntimeException {
    public UserDisabledException(String username) {
        super("User with username='" + username + "' is disabled");
    }
}
