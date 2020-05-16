package com.bugyip.game.controller;

import com.bugyip.game.enums.GameStatus;
import com.bugyip.game.enums.RoundStatus;
import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.model.request.StartGameRequestModel;
import com.bugyip.game.model.response.PlayRoundResponseModel;
import com.bugyip.game.model.response.StartGameResponseModel;
import com.bugyip.game.service.NumberGuessingGameService;
import com.bugyip.game.service.dto.PlayRoundResponseDto;
import com.bugyip.game.service.dto.StartGameResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class GuessingGameControllerTest {

    @InjectMocks
    private GuessingGameController guessingGameController;

    @Mock
    private Environment environment;

    @Mock
    private NumberGuessingGameService numberGuessingGameService;

    private StartGameRequestModel startGameRequestModel;

    private PlayRoundRequestModel playRoundRequestModel;

    private StartGameResponseDto startGameResponseDto;

    private PlayRoundResponseDto playRoundResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        final String GAME_TYPE_EASY = "easy";
        startGameRequestModel = new StartGameRequestModel(5, GAME_TYPE_EASY);
        playRoundRequestModel = new PlayRoundRequestModel(1L, 7);
        startGameResponseDto = new StartGameResponseDto(1L, 1, 20);
        playRoundResponseDto = new PlayRoundResponseDto(2L, 3, RoundStatus.HIGH, GameStatus.RUNNING);
    }

    @Test
    public void testStartNewGame() {
        when(numberGuessingGameService.startNewGame(any(StartGameRequestModel.class))).thenReturn(startGameResponseDto);

        ResponseEntity<StartGameResponseModel> responseEntity = guessingGameController.startNewGame(startGameRequestModel);
        StartGameResponseModel startGameResponseModel = responseEntity.getBody();

        assertNotNull(startGameResponseModel);
        assertNotNull(startGameResponseModel.getGameId());
        assertNotNull(startGameResponseModel.getRangeFrom());
        assertNotNull(startGameResponseModel.getRangeTo());
        assertEquals(startGameResponseModel.getRangeFrom(), startGameResponseDto.getRangeFrom());
        assertEquals(startGameResponseModel.getRangeTo(), startGameResponseDto.getRangeTo());
    }

    @Test
    public void testPlayNewRound() {
        when(numberGuessingGameService.playNewRound(any(PlayRoundRequestModel.class))).thenReturn(playRoundResponseDto);

        ResponseEntity<PlayRoundResponseModel> responseEntity = guessingGameController.playNewRound(playRoundRequestModel);
        PlayRoundResponseModel playRoundResponseModel = responseEntity.getBody();

        assertNotNull(playRoundResponseModel);
        assertNotNull(playRoundResponseModel.getRemainingRounds());
        assertNotNull(playRoundResponseModel.getRoundId());
    }
}
