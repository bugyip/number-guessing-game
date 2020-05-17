package com.bugyip.game.service.dto;

public class StartGameResponseDto {

    private Long gameId;

    private Integer rangeFrom;

    private Integer rangeTo;

    public StartGameResponseDto(Long gameId, Integer rangeFrom, Integer rangeTo) {
        this.gameId = gameId;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    public Long getGameId() {
        return gameId;
    }

    public Integer getRangeFrom() {
        return rangeFrom;
    }

    public Integer getRangeTo() {
        return rangeTo;
    }
}
