package ru.funbox.links.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.funbox.links.web.RestResponse;
import ru.funbox.links.web.rest.errors.BadRequestAlertException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> unhandledExceptions(
            Exception ex,
            HttpServletRequest httpServletRequest
    ) {
        return new ResponseEntity<>(
                new RestResponse(ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestAlertException.class)
    public ResponseEntity<RestResponse> badRequestException(
            Exception ex,
            HttpServletRequest httpServletRequest
    ) {
        return new ResponseEntity<>(
                new RestResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
