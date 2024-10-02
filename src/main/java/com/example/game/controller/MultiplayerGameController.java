package com.example.game.controller;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.enume.Choices;
import com.example.game.service.MultiplayerGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/multiplayer")
public class MultiplayerGameController {

    private final MultiplayerGameService multiplayerGameService;

    @PostMapping("/create")
    public MultiplayerGame createGame(@RequestParam String player1, @RequestParam String player2) {
        return multiplayerGameService.createGame(player1, player2);
    }

    @PostMapping("/play/{gameId}")
    public String playRound(@PathVariable Long gameId, @RequestParam String username, @RequestParam String choice) {
        Choices playerChoice = Choices.valueOf(choice.toUpperCase());
        return multiplayerGameService.playRound(gameId, username, playerChoice);
    }

    @GetMapping("/user/{userId}/history")
    public List<MultiplayerGame> getUserGameHistory(@PathVariable Long userId) {
        return multiplayerGameService.getUserGameHistory(userId);
    }
}
