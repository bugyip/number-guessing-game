package com.bugyip.game.model.request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * In case of missing maxNumberOfRounds a ConstraintViolation should be thrown.
     */
    @Test
    public void testStartNewGame_MaxNumberOfRoundsIsNull_ConstraintViolationException() {
        StartGameRequestModel startGameRequestModel = new StartGameRequestModel(null, null);

        Set<ConstraintViolation<StartGameRequestModel>> violations = validator.validate(startGameRequestModel);
        assertEquals(violations.size(), 1);
    }

    /**
     * In case of providing a null trial number a ConstraintViolation should be thrown.
     */
    @Test
    public void testplayNewRound_TrialNumberIsNull_ConstraintViolationException() {
        PlayRoundRequestModel playRoundRequestModel = new PlayRoundRequestModel(1L, null);

        Set<ConstraintViolation<PlayRoundRequestModel>> violations = validator.validate(playRoundRequestModel);
        assertEquals(violations.size(), 1);
    }

    /**
     * In case of providing a null game id a ConstraintViolation should be thrown.
     */
    @Test
    public void testplayNewRound_GameIdIsNull_ConstraintViolationException(){
        PlayRoundRequestModel playRoundRequestModel = new PlayRoundRequestModel(null, 1);

        Set<ConstraintViolation<PlayRoundRequestModel>> violations = validator.validate(playRoundRequestModel);
        assertEquals(violations.size(), 1);
    }
}
