package com.bugyip.game.service.impl;

import com.bugyip.game.entity.GameEntity;
import com.bugyip.game.entity.GameTypeEntity;
import com.bugyip.game.entity.RoundEntity;
import com.bugyip.game.enums.DifficultyType;
import com.bugyip.game.enums.GameStatus;
import com.bugyip.game.enums.RoundStatus;
import com.bugyip.game.exception.GameWasFinishedException;
import com.bugyip.game.exception.NumberOutOfRangeException;
import com.bugyip.game.exception.NotFoundException;
import com.bugyip.game.model.request.StartGameRequestModel;
import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.repository.GameEntityRepository;
import com.bugyip.game.repository.GameTypeEntityRepository;
import com.bugyip.game.repository.RoundEntityRepository;
import com.bugyip.game.service.NumberGuessingGameService;
import com.bugyip.game.service.dto.StartGameResponseDto;
import com.bugyip.game.service.dto.PlayRoundResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class NumberGuessingGameServiceImpl implements NumberGuessingGameService {

    private GameEntityRepository gameEntityRepository;

    private GameTypeEntityRepository gameTypeEntityRepository;

    private RoundEntityRepository roundEntityRepository;

    @Autowired
    public NumberGuessingGameServiceImpl(GameTypeEntityRepository gameTypeEntityRepository, GameEntityRepository gameEntityRepository, RoundEntityRepository roundEntityRepository) {
        this.gameTypeEntityRepository = gameTypeEntityRepository;
        this.gameEntityRepository = gameEntityRepository;
        this.roundEntityRepository = roundEntityRepository;
    }

    /**
     * Start a new game and store it.
     * @param startGameRequestModel
     * @return StartGameResponseDto (game id, range info)
     */
    @Override
    @Transactional
    public StartGameResponseDto startNewGame(StartGameRequestModel startGameRequestModel) {

        GameTypeEntity gameTypeEntity;

        if (startGameRequestModel.getDifficulty() == null) {
            gameTypeEntity = gameTypeEntityRepository.findByDifficultyType(DifficultyType.DEFAULT);
        } else {
            try {
                gameTypeEntity = gameTypeEntityRepository.findByDifficultyType(DifficultyType.valueOf(startGameRequestModel.getDifficulty().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new NotFoundException(String.format("There is no difficulty with the given name. (%s)", startGameRequestModel.getDifficulty()));
            }
        }

        if (gameTypeEntity == null) {
            throw new NotFoundException(String.format("The selected difficulty (%s) not found!",
                    startGameRequestModel.getDifficulty() != null ? DifficultyType.valueOf(startGameRequestModel.getDifficulty()) : null));
        }

        GameEntity savedGameEntity = gameEntityRepository.save(new GameEntity(generateRandomNumberBetweenRange(gameTypeEntity.getRangeFrom(), gameTypeEntity.getRangeTo()),
                startGameRequestModel.getMaxNumberOfRounds(),
                GameStatus.RUNNING,
                gameTypeEntity));

        return new StartGameResponseDto(savedGameEntity.getId(), gameTypeEntity.getRangeFrom(), gameTypeEntity.getRangeTo());
    }

    /**
     * Play a new round. The round information will be saved.
     * @param guessingRequestModel
     * @return PlayRoundResponseDto (round id, remaining rounds, round status game status)
     */
    @Override
    @Transactional
    public PlayRoundResponseDto playNewRound(PlayRoundRequestModel guessingRequestModel) {
        RoundStatus roundStatus = null;
        Optional<GameEntity> gameEntityOpt = gameEntityRepository.findById(guessingRequestModel.getGameId());
        GameEntity gameEntity = gameEntityOpt.isPresent() ? gameEntityOpt.get() : null;
        GameTypeEntity gameTypeEntity = gameEntity != null ? gameEntity.getGameTypeEntity() : null;

        if (gameTypeEntity == null) {
            throw  new NotFoundException(String.format("The selected game (game id = %s) not found!", guessingRequestModel.getGameId()));
        } else if (!GameStatus.RUNNING.equals(gameEntity.getGameStatus())) {
            throw new GameWasFinishedException(String.format("The game has already finished! (game id = %s)", gameEntity.getId()));
        } else if (guessingRequestModel.getTrialNumber() < gameTypeEntity.getRangeFrom() || guessingRequestModel.getTrialNumber() > gameTypeEntity.getRangeTo()) {
            throw new NumberOutOfRangeException(String.format("The trial number is out of the range (%s - %s)", gameTypeEntity.getRangeFrom(), gameTypeEntity.getRangeTo()));
        }

        int trialNumber = guessingRequestModel.getTrialNumber();

        if (trialNumber == gameEntity.getHiddenNumber()) {
            roundStatus = RoundStatus.MATCH;
        } else if (trialNumber < gameEntity.getHiddenNumber()) {
            roundStatus = RoundStatus.LOW;
        } else if (trialNumber > gameEntity.getHiddenNumber()) {
            roundStatus = RoundStatus.HIGH;
        }

        RoundEntity savedRoundEntity = roundEntityRepository.save(new RoundEntity(new Date(), trialNumber));
        gameEntity.addRoundEntity(savedRoundEntity);

        if (RoundStatus.MATCH.equals(roundStatus)) {
            gameEntity.setGameStatus(GameStatus.WON);
        } else if (isLastRound(gameEntity)) {
            gameEntity.setGameStatus(GameStatus.LOST);
        }

        return new PlayRoundResponseDto(savedRoundEntity.getId(), gameEntity.getMaxNumberOfRounds() - gameEntity.getRoundEntityList().size(), roundStatus, gameEntity.getGameStatus());
    }

    private boolean isLastRound(GameEntity gameEntity) {
        return gameEntity.getMaxNumberOfRounds() == gameEntity.getRoundEntityList().size();
    }

    private int generateRandomNumberBetweenRange(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}