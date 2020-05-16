package com.bugyip.game.entity;

import com.bugyip.game.enums.GameStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
public class GameEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "hidden_number")
    private Integer hiddenNumber;

    @Column(name = "max_number_of_rounds")
    private Integer maxNumberOfRounds;

    @Column(name = "game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @OneToOne
    @JoinColumn(name = "game_type_id")
    private GameTypeEntity gameTypeEntity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private List<RoundEntity> roundEntityList;

    public GameEntity() {
    }

    public GameEntity(Integer hiddenNumber, Integer maxNumberOfRounds, GameStatus gameStatus, GameTypeEntity gameTypeEntity) {
        this.hiddenNumber = hiddenNumber;
        this.maxNumberOfRounds = maxNumberOfRounds;
        this.gameStatus = gameStatus;
        this.gameTypeEntity = gameTypeEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHiddenNumber() {
        return hiddenNumber;
    }

    public void setHiddenNumber(Integer hiddenNumber) {
        this.hiddenNumber = hiddenNumber;
    }

    public Integer getMaxNumberOfRounds() {
        return maxNumberOfRounds;
    }

    public void setMaxNumberOfRounds(Integer maxNumberOfRounds) {
        this.maxNumberOfRounds = maxNumberOfRounds;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public GameTypeEntity getGameTypeEntity() {
        return gameTypeEntity;
    }

    public void setGameTypeEntity(GameTypeEntity gameTypeEntity) {
        this.gameTypeEntity = gameTypeEntity;
    }

    public List<RoundEntity> getRoundEntityList() {
        return roundEntityList;
    }

    public void setRoundEntityList(List<RoundEntity> roundEntityList) {
        this.roundEntityList = roundEntityList;
    }

    public void addRoundEntity(RoundEntity roundEntity) {

        if (roundEntityList == null) {
            roundEntityList = new ArrayList<>();
        }

        roundEntityList.add(roundEntity);
    }

}
