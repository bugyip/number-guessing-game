package com.bugyip.game.exception;

/**
 * This exception is thrown when the selected game has already finished.
 */
public class GameWasFinishedException extends RuntimeException {

    public GameWasFinishedException(String message) {
        super(message);
    }
}
