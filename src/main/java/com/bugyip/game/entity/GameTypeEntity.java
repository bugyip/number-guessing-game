package com.bugyip.game.entity;

import com.bugyip.game.enums.DifficultyType;

import javax.persistence.*;

@Entity
@Table(name = "game_type")
public class GameTypeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, name = "difficulty")
    @Enumerated(EnumType.STRING)
    private DifficultyType difficultyType;

    @Column(name = "range_from")
    private Integer rangeFrom;

    @Column(name = "range_to")
    private Integer rangeTo;

    public GameTypeEntity() {
    }

    public GameTypeEntity(DifficultyType difficultyType, Integer rangeFrom, Integer rangeTo) {
        this.difficultyType = difficultyType;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DifficultyType getDifficultyType() {
        return difficultyType;
    }

    public void setDifficultyType(DifficultyType difficultyType) {
        this.difficultyType = difficultyType;
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
