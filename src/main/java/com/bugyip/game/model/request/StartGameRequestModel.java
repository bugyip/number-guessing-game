package com.bugyip.game.model.request;

import javax.validation.constraints.NotNull;

/**
 * Request model for starting a new game
 * {@link com.bugyip.game.controller.GuessingGameController#startNewGame(StartGameRequestModel)}
 */
public class StartGameRequestModel {

    @NotNull(message = "is required")
    private Integer maxNumberOfRounds;

    private String difficulty;

    public StartGameRequestModel(Integer maxNumberOfRounds, String difficulty) {
        this.maxNumberOfRounds = maxNumberOfRounds;
        this.difficulty = difficulty;
    }

    public Integer getMaxNumberOfRounds() {
        return maxNumberOfRounds;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
