package com.bugyip.game.service.dto;

import com.bugyip.game.enums.GameStatus;
import com.bugyip.game.enums.RoundStatus;

public class PlayRoundResponseDto {

    private Long roundId;

    private Integer remainingRounds;

    private RoundStatus roundStatus;

    private GameStatus gameStatus;

    public PlayRoundResponseDto(Long roundId, Integer remainingRounds, RoundStatus roundStatus, GameStatus gameStatus) {
        this.roundId = roundId;
        this.remainingRounds = remainingRounds;
        this.roundStatus = roundStatus;
        this.gameStatus = gameStatus;
    }

    public Long getRoundId() {
        return roundId;
    }

    public Integer getRemainingRounds() {
        return remainingRounds;
    }

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
