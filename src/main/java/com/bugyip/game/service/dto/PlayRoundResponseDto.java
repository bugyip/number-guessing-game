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

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public Integer getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(Integer remainingRounds) {
        this.remainingRounds = remainingRounds;
    }

    public RoundStatus getRoundStatus() {
        return roundStatus;
    }

    public void setRoundStatus(RoundStatus roundStatus) {
        this.roundStatus = roundStatus;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
