package com.bugyip.game.service.impl;

import com.bugyip.game.entity.GameEntity;
import com.bugyip.game.entity.GameTypeEntity;
import com.bugyip.game.entity.RoundEntity;
import com.bugyip.game.enums.DifficultyType;
import com.bugyip.game.enums.GameStatus;
import com.bugyip.game.enums.RoundStatus;
import com.bugyip.game.exception.GameWasFinishedException;
import com.bugyip.game.exception.NotFoundException;
import com.bugyip.game.exception.NumberOutOfRangeException;
import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.model.request.StartGameRequestModel;
import com.bugyip.game.repository.GameEntityRepository;
import com.bugyip.game.repository.GameTypeEntityRepository;
import com.bugyip.game.repository.RoundEntityRepository;
import com.bugyip.game.service.dto.PlayRoundResponseDto;
import com.bugyip.game.service.dto.StartGameResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class NumberGuessingGameServiceImplTest {

    @InjectMocks
    private NumberGuessingGameServiceImpl numberGuessingGameService;

    @Mock
    private GameTypeEntityRepository gameTypeEntityRepository;

    @Mock
    private GameEntityRepository gameEntityRepository;

    @Mock
    private RoundEntityRepository roundEntityRepository;

    private GameTypeEntity defaultGameType;

    private StartGameRequestModel startDefaultGameRequestModel;

    private PlayRoundRequestModel playRoundRequestModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        defaultGameType = new GameTypeEntity(DifficultyType.DEFAULT, 1, 10);
        startDefaultGameRequestModel = new StartGameRequestModel(5, null);
        playRoundRequestModel = new PlayRoundRequestModel(1L, 4);
    }

    /**
     * Start a new game -> game not found by id -> NotFoundException should be thrown.
     */
    @Test
    public void testStartNewGame_NoAnyGameType_NotFoundException() {
        when(gameTypeEntityRepository.findByDifficultyType(any(DifficultyType.class))).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> {
                    numberGuessingGameService.startNewGame(startDefaultGameRequestModel);
                });
    }

    /**
     * Start a new game ('default' game type)
     * response range from = 'default' game type from
     * response range to = 'default' game type to
     */
    @Test
    public void testStartNewGame_GameTypeIsNull_SetDefaultGameType() {
        when(gameTypeEntityRepository.findByDifficultyType(any(DifficultyType.class))).thenReturn(defaultGameType);
        when(gameEntityRepository.save(any())).thenReturn(getStartedGameEntity());

        StartGameResponseDto startGameResponseDto = numberGuessingGameService.startNewGame(startDefaultGameRequestModel);

        assertEquals(startGameResponseDto.getRangeFrom(), defaultGameType.getRangeFrom());
        assertEquals(startGameResponseDto.getRangeTo(), defaultGameType.getRangeTo());
    }


    /**
     * Start a new round -> game not found -> NotFoundException should be thrown.
     */
    @Test
    public void testplayNewRound_NotExistingGameId_NotFoundException() {
        when(gameEntityRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> {
                    numberGuessingGameService.playNewRound(playRoundRequestModel = new PlayRoundRequestModel(1L, 1));
                });
    }

    /**
     * Running game
     * Round 1 (trial < lower bound of the range)
     * Round 2 (trial > higher bound of the range)
     */
    @Test
    public void testplayNewRound_FinishedGameId_NumberOutOfRangeException() {
        GameTypeEntity gameTypeEntity = getStartedGameEntity().getGameTypeEntity();

        when(gameEntityRepository.findById(anyLong())).thenReturn(Optional.of(getStartedGameEntity()));

        assertThrows(NumberOutOfRangeException.class,
                () -> {
                    numberGuessingGameService.playNewRound(new PlayRoundRequestModel(1L, gameTypeEntity.getRangeFrom() - 1));
                    numberGuessingGameService.playNewRound(new PlayRoundRequestModel(1L, gameTypeEntity.getRangeTo() + 1));
                });
    }

    /**
     * Finished game -> one more round -> GameWasFinishedException should be thrown
     */
    @Test
    public void testplayNewRound_TrialNumberOutOfRange_GameWasFinishedException() {
        when(gameEntityRepository.findById(anyLong())).thenReturn(Optional.of(getFinishedGameEntity()));

        assertThrows(GameWasFinishedException.class,
                () -> {
                    numberGuessingGameService.playNewRound(playRoundRequestModel);
                });
    }

    /**
     * Losing in the last round.
     * Accepted statuses:
     * GameStatus = LOST
     * RoundStatus = LOW or HIGH
     */
    @Test
    public void testplayNewRound_LastRound_LoseTheGame() {
        PlayRoundRequestModel roundRequestModel = new PlayRoundRequestModel(1L, 4);
        PlayRoundResponseDto playRoundResponseDto;

        whenFindGameAndSaveRoundRun(getLastRoundGameEntity(), new RoundEntity(new Date(), roundRequestModel.getTrialNumber()));

        // Last trial number = 4, hidden number = 5 -> LOSE
        playRoundResponseDto = numberGuessingGameService.playNewRound(playRoundRequestModel);
        assertEquals(playRoundResponseDto.getRemainingRounds(), 0);
        assertEquals(playRoundResponseDto.getGameStatus(), GameStatus.LOST);
        assertEquals(playRoundResponseDto.getRoundStatus(), RoundStatus.LOW);
    }

    /**
     * Winning in the last round.
     * Accepted statuses:
     * GameStatus = WON
     * RoundStatus = MATCH
     */
    @Test
    public void testplayNewRound_LastRound_WinTheGame() {
        PlayRoundRequestModel winnerRoundRequestModel = new PlayRoundRequestModel(1L, 5);
        PlayRoundResponseDto playRoundResponseDto;

        whenFindGameAndSaveRoundRun(getLastRoundGameEntity(), new RoundEntity(new Date(), winnerRoundRequestModel.getTrialNumber()));

        // Last trial number = 5, hidden number = 5 -> WIN
        playRoundResponseDto = numberGuessingGameService.playNewRound(winnerRoundRequestModel);
        assertEquals(playRoundResponseDto.getRemainingRounds(), 0);
        assertEquals(playRoundResponseDto.getGameStatus(), GameStatus.WON);
        assertEquals(playRoundResponseDto.getRoundStatus(), RoundStatus.MATCH);
    }

    /**
     * Winning in an intermediate round.
     * Accepted statuses:
     * GameStatus = WON
     * RoundStatus = MATCH
     */
    @Test
    public void testplayNewRound_IntermediateRound_WinTheGame() {
        PlayRoundRequestModel winnerRoundRequestModel = new PlayRoundRequestModel(1L, 5);
        PlayRoundResponseDto playRoundResponseDto;

        GameEntity startedGameEntity = getStartedGameEntity();
        whenFindGameAndSaveRoundRun(startedGameEntity, new RoundEntity(new Date(), winnerRoundRequestModel.getTrialNumber()));

        // Last trial number = 5, hidden number = 5 -> WIN
        playRoundResponseDto = numberGuessingGameService.playNewRound(winnerRoundRequestModel);
        assertEquals(playRoundResponseDto.getRemainingRounds(), startedGameEntity.getMaxNumberOfRounds() - 1);
        assertEquals(playRoundResponseDto.getGameStatus(), GameStatus.WON);
        assertEquals(playRoundResponseDto.getRoundStatus(), RoundStatus.MATCH);
    }

    /**
     * Winning in an intermediate round.
     * Accepted statuses:
     * GameStatus = RUNNING
     * RoundStatus = LOW
     */
    @Test
    public void testplayNewRound_IntermediateRound_LowRoundStatus() {
        PlayRoundRequestModel lowRoundRequestModel = new PlayRoundRequestModel(1L, 1);
        PlayRoundResponseDto playRoundResponseDto;

        whenFindGameAndSaveRoundRun(getStartedGameEntity(), new RoundEntity(new Date(), lowRoundRequestModel.getTrialNumber()));

        // Last trial number = 1, hidden number = 5
        playRoundResponseDto = numberGuessingGameService.playNewRound(lowRoundRequestModel);
        assertEquals(playRoundResponseDto.getGameStatus(), GameStatus.RUNNING);
        assertEquals(playRoundResponseDto.getRoundStatus(), RoundStatus.LOW);
    }

    /**
     * Guessing high in an intermediate round.
     * Accepted statuses:
     * GameStatus = RUNNING
     * RoundStatus = HIGH
     */
    @Test
    public void testplayNewRound_IntermediateRound_HighRoundStatus() {
        PlayRoundRequestModel highRoundRequestModel = new PlayRoundRequestModel(1L, 10);
        PlayRoundResponseDto playRoundResponseDto;

        whenFindGameAndSaveRoundRun(getStartedGameEntity(), new RoundEntity(new Date(), highRoundRequestModel.getTrialNumber()));

        // Last trial number = 10, hidden number = 5
        playRoundResponseDto = numberGuessingGameService.playNewRound(highRoundRequestModel);
        assertEquals(playRoundResponseDto.getGameStatus(), GameStatus.RUNNING);
        assertEquals(playRoundResponseDto.getRoundStatus(), RoundStatus.HIGH);
    }

    private void whenFindGameAndSaveRoundRun(GameEntity gameEntity, RoundEntity roundEntity) {
        when(gameEntityRepository.findById(anyLong())).thenReturn(Optional.of(gameEntity));
        when(roundEntityRepository.save(any())).thenReturn(roundEntity);
    }

    private GameEntity getFinishedGameEntity() {
        GameEntity finishedRoundGameEntity = new GameEntity();
        finishedRoundGameEntity.setId(1L);
        finishedRoundGameEntity.setHiddenNumber(5);
        finishedRoundGameEntity.setMaxNumberOfRounds(3);
        finishedRoundGameEntity.setGameStatus(GameStatus.WON);
        finishedRoundGameEntity.setGameTypeEntity(defaultGameType);
        finishedRoundGameEntity.addRoundEntity(new RoundEntity(new Date(), 1));
        finishedRoundGameEntity.addRoundEntity(new RoundEntity(new Date(), 2));
        finishedRoundGameEntity.addRoundEntity(new RoundEntity(new Date(), 3));

        return finishedRoundGameEntity;
    }

    private GameEntity getLastRoundGameEntity() {
        GameEntity lastRoundGameEntity = new GameEntity();
        lastRoundGameEntity.setId(1L);
        lastRoundGameEntity.setHiddenNumber(5);
        lastRoundGameEntity.setMaxNumberOfRounds(3);
        lastRoundGameEntity.setGameStatus(GameStatus.RUNNING);
        lastRoundGameEntity.setGameTypeEntity(defaultGameType);
        lastRoundGameEntity.addRoundEntity(new RoundEntity(new Date(), 1));
        lastRoundGameEntity.addRoundEntity(new RoundEntity(new Date(), 2));

        return lastRoundGameEntity;
    }

    private GameEntity getStartedGameEntity() {
        GameEntity runningGameEntity = new GameEntity();
        runningGameEntity.setId(1L);
        runningGameEntity.setHiddenNumber(5);
        runningGameEntity.setMaxNumberOfRounds(10);
        runningGameEntity.setGameStatus(GameStatus.RUNNING);
        runningGameEntity.setGameTypeEntity(defaultGameType);

        return runningGameEntity;
    }
}
