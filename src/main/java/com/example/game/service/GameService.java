package com.example.game.service;

import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.model.GameChoice;
import com.example.game.repository.GameChoiceRepository;
import com.example.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameChoiceRepository gameChoiceRepository;
    private final Choices[] choices = Choices.values();
    private final UserRepository userRepository;

    public GameChoice playGame(Choices playerChoice, String username) {
          Random random = new Random();
        Choices computerChoice = choices[random.nextInt(choices.length)];

        String result = determineWinner(playerChoice, computerChoice);
        User user = userRepository.findByUsername(username);

        if (result.equals("You win!"))
            user.setScore(user.getScore() + 1);
        userRepository.save(user);

        GameChoice gameChoice = GameChoice.builder()
                .playerChoice(playerChoice).computerChoice(computerChoice)
                .result(result).time(LocalDateTime.now()).user(user)
                .build();
        gameChoiceRepository.save(gameChoice);
        return gameChoice;
    }

    private String determineWinner(Choices player, Choices computer) {
        if (player == computer) {
            return "It's a tie!";
        } else if ((player == Choices.ROCK && computer == Choices.SCISSORS) ||
                (player == Choices.SCISSORS && computer == Choices.PAPER) ||
                (player == Choices.PAPER && computer == Choices.ROCK)) {
            return "You win!";
        } else {
            return "You lose!";
        }
    }
}