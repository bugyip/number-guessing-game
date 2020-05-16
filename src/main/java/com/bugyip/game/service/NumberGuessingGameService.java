package com.bugyip.game.service;

import com.bugyip.game.model.request.StartGameRequestModel;
import com.bugyip.game.model.request.PlayRoundRequestModel;
import com.bugyip.game.service.dto.StartGameResponseDto;
import com.bugyip.game.service.dto.PlayRoundResponseDto;

public interface NumberGuessingGameService {

    StartGameResponseDto startNewGame(StartGameRequestModel startGameRequestModel);

    PlayRoundResponseDto playNewRound(PlayRoundRequestModel guessingRequestModel);
}
