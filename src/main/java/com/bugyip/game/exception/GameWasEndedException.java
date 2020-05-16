package com.bugyip.game.exception;

/**
 * This exception is thrown when the selected game has already ended.
 */
public class GameWasEndedException extends RuntimeException {

    public GameWasEndedException(String message) {
        super(message);
    }
}
