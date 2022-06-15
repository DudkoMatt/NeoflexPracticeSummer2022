package ru.dudkomv.neoflexpractice.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.dudkomv.neoflexpractice.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Data
    @Builder
    @JsonInclude(value = NON_NULL)
    @AllArgsConstructor
    @Schema(description = "Api error")
    public static class ApiError {
        @Schema(description = "Error message")
        private String message;

        @Schema(description = "List of errors")
        private List<String> errors;
    }

    private static final String JSON_MALFORMED_MESSAGE = "JSON object malformed. Check DTO schema in OpenAPI docs.";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error.";

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return new ResponseEntity<>(
                ApiError
                        .builder()
                        .errors(formatListOfErrorsFromArgumentNotValidException(ex))
                        .build(),
                status
        );
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return new ResponseEntity<>(
                ApiError
                        .builder()
                        .message(JSON_MALFORMED_MESSAGE)
                        .build(),
                status
        );
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             @Nullable Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        return new ResponseEntity<>(
                ApiError
                        .builder()
                        .message(INTERNAL_SERVER_ERROR_MESSAGE)
                        .build(),
                status
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(
                ApiError
                        .builder()
                        .message(ex.getMessage())
                        .build(),
                NOT_FOUND
        );
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ApiError> handlePSQLException(PSQLException ex) {
        return new ResponseEntity<>(
                ApiError
                        .builder()
                        .message(ex.getMessage())
                        .build(),
                BAD_REQUEST
        );
    }

    private static List<String> formatListOfErrorsFromArgumentNotValidException(MethodArgumentNotValidException ex) {
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return errors;
    }
}
