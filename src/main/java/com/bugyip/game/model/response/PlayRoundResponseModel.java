package com.bugyip.game.model.response;

import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.model.request.StartGameRequestModel;

/**
 * Request model for playing a new round
 * {@link com.bugyip.game.controller.GuessingGameController#playNewRound(PlayRoundRequestModel)}
 */
public class PlayRoundResponseModel {

    private Long roundId;

    private Integer remainingRounds;

    private String message;

    public PlayRoundResponseModel(Long roundId, Integer remainingRounds, String message) {
        this.roundId = roundId;
        this.remainingRounds = remainingRounds;
        this.message = message;
    }

    public Long getRoundId() {
        return roundId;
    }

    public Integer getRemainingRounds() {
        return remainingRounds;
    }

    public String getMessage() {
        return message;
    }
}
