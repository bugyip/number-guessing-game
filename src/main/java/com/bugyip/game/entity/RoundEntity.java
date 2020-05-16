package com.bugyip.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "round")
public class RoundEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date_of_guess")
    private Date dateOfGuess;

    @Column(name = "trial_number")
    private Integer trialNumber;

    public RoundEntity() {
    }

    public RoundEntity(Date dateOfGuess, Integer trialNumber) {
        this.dateOfGuess = dateOfGuess;
        this.trialNumber = trialNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfGuess() {
        return dateOfGuess;
    }

    public void setDateOfGuess(Date dateOfGuess) {
        this.dateOfGuess = dateOfGuess;
    }

    public Integer getTrialNumber() {
        return trialNumber;
    }

    public void setTrialNumber(Integer trialNumber) {
        this.trialNumber = trialNumber;
    }
}
