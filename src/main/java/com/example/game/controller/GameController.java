package com.example.game.controller;

import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.model.GameChoice;
import com.example.game.repository.UserRepository;
import com.example.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final UserRepository userRepository;
    private final GameService gameService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return "Login successful";
        }

        return "Invalid password OR username";
    }

    @PostMapping("/play")
    public GameChoice play(@RequestParam String choice, @RequestParam String username) {
        Choices playerChoice = Choices.valueOf(choice.toUpperCase());
        return gameService.playGame(playerChoice, username);
    }

    @GetMapping("/board")
    public List<User> getLeaderboard() {
        return userRepository.findTop10ByOrderByScoreDesc();
    }
}