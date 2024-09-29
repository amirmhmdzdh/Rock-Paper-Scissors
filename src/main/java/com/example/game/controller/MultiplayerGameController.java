package com.example.game.controller;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.repository.MultiplayerGameRepository;
import com.example.game.repository.UserRepository;
import com.example.game.service.MultiplayerGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multiplayer")
@RequiredArgsConstructor
public class MultiplayerGameController {

    private final MultiplayerGameService multiplayerGameService;
    private final MultiplayerGameRepository multiplayerGameRepository;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public MultiplayerGame createGame(@RequestParam String player1, @RequestParam String player2) {
        return multiplayerGameService.createGame(player1, player2);
    }

    @PostMapping("/play/{gameId}")
    public String playRound(@PathVariable Long gameId, @RequestParam String username, @RequestParam String choice) {
        Choices playerChoice = Choices.valueOf(choice.toUpperCase());
        return multiplayerGameService.playRound(gameId, username, playerChoice);
    }

    @GetMapping("/{gameId}")
    public MultiplayerGame getGame(@PathVariable Long gameId) {
        return multiplayerGameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @GetMapping("/leaderboard")
    public List<User> getLeaderboard() {
        return userRepository.findTop10ByOrderByScoreDesc();
    }
}