package com.bugyip.game.model.request;

import javax.validation.constraints.NotNull;

/**
 * Request model for playing a new round
 * {@link com.bugyip.game.controller.GuessingGameController#playNewRound(PlayRoundRequestModel)}
 */
public class PlayRoundRequestModel {

    /**
     * id of the played game
     */
    @NotNull(message = "is required")
    private Long gameId;

    /**
     * the guessing of the actual round
     */
    @NotNull(message = "is required")
    private Integer trialNumber;

    public PlayRoundRequestModel(Long gameId, Integer trialNumber) {
        this.gameId = gameId;
        this.trialNumber = trialNumber;
    }

    public Long getGameId() {
        return gameId;
    }

    public Integer getTrialNumber() {
        return trialNumber;
    }
}
