package com.bugyip.game.controller;

import com.bugyip.game.enums.GameStatus;
import com.bugyip.game.model.request.StartGameRequestModel;
import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.model.response.PlayRoundResponseModel;
import com.bugyip.game.model.response.StartGameResponseModel;
import com.bugyip.game.service.NumberGuessingGameService;
import com.bugyip.game.service.dto.StartGameResponseDto;
import com.bugyip.game.service.dto.PlayRoundResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/guessing")
public class GuessingGameController {

    private Environment environment;

    private NumberGuessingGameService numberGuessingGameService;

    @Autowired
    public GuessingGameController(Environment environment, NumberGuessingGameService numberGuessingGameService) {
        this.numberGuessingGameService = numberGuessingGameService;
        this.environment = environment;
    }

    /**
     * Start a new game with the provided init parameters
     * @param StartGameRequestModel - Contains the game init paramaters (maxNumberOfRounds, gameType)
     * @return a generated String id for the started game
     */
    @PostMapping("/games")
    public ResponseEntity<StartGameResponseModel> startNewGame(@Valid @RequestBody StartGameRequestModel StartGameRequestModel) {
        StartGameResponseDto startGameResponseDto = numberGuessingGameService.startNewGame(StartGameRequestModel);
        return new ResponseEntity<>(new StartGameResponseModel(startGameResponseDto.getGameId(), startGameResponseDto.getRangeFrom(), startGameResponseDto.getRangeTo()), HttpStatus.CREATED);
    }

    /**
     * Start a new round in the game specified by the game id
     * @param guessingRequestModel - Contains the game id and the trial number
     * @return a String message in relation with the accuracy of the actual trial
     */
    @PostMapping("/rounds")
    public ResponseEntity<PlayRoundResponseModel> playNewRound(@Valid @RequestBody PlayRoundRequestModel guessingRequestModel) {
        PlayRoundResponseDto playRoundResponseDto = numberGuessingGameService.playNewRound(guessingRequestModel);

        String message = environment.getProperty(String.format("guessing.game.message.%s",
                !GameStatus.RUNNING.equals(playRoundResponseDto.getGameStatus()) ?
                        playRoundResponseDto.getGameStatus().toString().toLowerCase() :
                        playRoundResponseDto.getRoundStatus().toString().toLowerCase()));

        return new ResponseEntity<>(new PlayRoundResponseModel(playRoundResponseDto.getRoundId(), playRoundResponseDto.getRemainingRounds(), message), HttpStatus.CREATED);
    }
}
