package com.bugyip.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the actual guessing is not within the range.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberOutOfRangeException extends RuntimeException {

    public NumberOutOfRangeException(String message) {
        super(message);
    }
}
