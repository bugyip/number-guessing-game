package com.bugyip.game.model.response;

import com.bugyip.game.model.request.StartGameRequestModel;

/**
 * Request model for starting a new game
 * {@link com.bugyip.game.controller.GuessingGameController#startNewGame(StartGameRequestModel)}
 */
public class StartGameResponseModel {

    private Long gameId;

    private Integer rangeFrom;

    private Integer rangeTo;

    public StartGameResponseModel(Long gameId, Integer rangeFrom, Integer rangeTo) {
        this.gameId = gameId;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(Integer rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public Integer getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(Integer rangeTo) {
        this.rangeTo = rangeTo;
    }
}
