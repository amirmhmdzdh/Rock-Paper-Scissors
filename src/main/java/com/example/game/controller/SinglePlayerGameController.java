package com.example.game.controller;

import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.model.SinglePlayerGame;
import com.example.game.repository.UserRepository;
import com.example.game.service.SinglePlayerGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/singlePlayer")

public class SinglePlayerGameController {

    private final UserRepository userRepository;
    private final SinglePlayerGameService singlePlayerGameService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/play")
    public SinglePlayerGame play(@RequestParam String choice, @RequestParam String username) {
        Choices playerChoice = Choices.valueOf(choice.toUpperCase());
        return singlePlayerGameService.playSinglePlayerGame(playerChoice, username);
    }


    @GetMapping("/user/{userId}/history")
    public List<SinglePlayerGame> getUserGameHistory(@PathVariable Long userId) {
        return singlePlayerGameService.getUserGameHistory(userId);
    }
}