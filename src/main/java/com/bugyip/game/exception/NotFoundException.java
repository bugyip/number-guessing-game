package com.bugyip.game.exception;

/**
 * This exception is thrown when the specified game or game type not found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
