package ru.dudkomv.neoflexpractice.advice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import ru.dudkomv.neoflexpractice.advice.ExceptionControllerAdvice.ApiError;
import ru.dudkomv.neoflexpractice.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionControllerAdviceTest {
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @BeforeEach
    void setUp() {
        exceptionControllerAdvice = new ExceptionControllerAdvice();
    }

    @Test
    void handleMethodArgumentNotValid() {
        Map<?, ?> expectedErrors = Mockito.mock(Map.class);

        BindingResult bindingResult = new MapBindingResult(expectedErrors, "");
        bindingResult.addError(new FieldError("", "A", "B"));
        bindingResult.addError(new FieldError("", "C", "D"));

        bindingResult.addError(new ObjectError("Global error", "message"));

        MethodParameter mockMethodParameter = Mockito.mock(MethodParameter.class);
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(mockMethodParameter, bindingResult);

        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<Object> responseEntity =
                exceptionControllerAdvice.handleMethodArgumentNotValid(exception, headers, status, webRequest);

        List<String> expectedListOfErrors = new ArrayList<>();
        expectedListOfErrors.add("A: B");
        expectedListOfErrors.add("C: D");
        expectedListOfErrors.add("Global error: message");

        ApiError expectedApiError = ApiError.builder()
                .errors(expectedListOfErrors)
                .build();

        assertEquals(status, responseEntity.getStatusCode());
        assertEquals(expectedApiError, responseEntity.getBody());
    }

    @Test
    void handleHttpMessageNotReadable() {
        HttpMessageNotReadableException exception = Mockito.mock(HttpMessageNotReadableException.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<Object> responseEntity =
                exceptionControllerAdvice.handleHttpMessageNotReadable(exception, headers, status, webRequest);

        ApiError expectedApiError = ApiError.builder()
                .message("JSON object malformed. Check DTO schema in OpenAPI docs.")
                .build();

        assertEquals(status, responseEntity.getStatusCode());
        assertEquals(expectedApiError, responseEntity.getBody());
    }

    @Test
    void handleExceptionInternal() {
        Exception exception = Mockito.mock(Exception.class);
        Object body = Mockito.mock(Object.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        WebRequest webRequest = Mockito.mock(WebRequest.class);

        ResponseEntity<Object> responseEntity =
                exceptionControllerAdvice.handleExceptionInternal(exception, body, headers, status, webRequest);

        ApiError expectedApiError = ApiError.builder()
                .message("Internal server error.")
                .build();

        assertEquals(status, responseEntity.getStatusCode());
        assertEquals(expectedApiError, responseEntity.getBody());
    }

    @Test
    void handleEntityNotFoundException() {
        String expectedMessage = "Some error message!";
        EntityNotFoundException entityNotFoundException = new EntityNotFoundException(expectedMessage);
        ResponseEntity<ApiError> responseEntity =
                exceptionControllerAdvice.handleEntityNotFoundException(entityNotFoundException);

        ApiError expectedApiError = ApiError.builder()
                .message(expectedMessage)
                .build();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(expectedApiError, responseEntity.getBody());
    }

    @Test
    void handlePSQLException() {
        String expectedMessage = "Some error message!";
        PSQLState state = PSQLState.UNKNOWN_STATE;
        PSQLException psqlException = new PSQLException(expectedMessage, state);

        ResponseEntity<ApiError> responseEntity =
                exceptionControllerAdvice.handlePSQLException(psqlException);

        ApiError expectedApiError = ApiError.builder()
                .message(expectedMessage)
                .build();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expectedApiError, responseEntity.getBody());
    }
}