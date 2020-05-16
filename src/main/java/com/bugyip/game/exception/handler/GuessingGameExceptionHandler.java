package com.bugyip.game.exception.handler;

import com.bugyip.game.exception.NumberOutOfRangeException;
import com.bugyip.game.exception.NotFoundException;
import com.bugyip.game.exception.GameWasEndedException;
import com.bugyip.game.exception.response.ExceptionResponse;
import com.bugyip.game.exception.response.MethodArgumentNotValidExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@RestController
public class GuessingGameExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *
     * @param ex - NotFoundException
     * @param request
     * @return ResponseEntity - HTTP 404
     */
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     *
     * @param ex - GameWasEndedException, NumberOutOfRangeException
     * @param request
     * @return - ResponseEntity - HTTP 400
     */
    @ExceptionHandler({GameWasEndedException.class, NumberOutOfRangeException.class})
    public final ResponseEntity<Object> handleGameWasEndedException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param ex - MethodArgumentNotValidException (in case of Hibernate validation)
     * @param headers
     * @param status
     * @param request
     * @return - ResponseEntity - HTTP 400
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();
        String error;

        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + " " + fieldError.getDefaultMessage();
            errors.add(error);
        }

        return new ResponseEntity(new MethodArgumentNotValidExceptionResponse(new Date(), errors), HttpStatus.BAD_REQUEST);
    }
}
