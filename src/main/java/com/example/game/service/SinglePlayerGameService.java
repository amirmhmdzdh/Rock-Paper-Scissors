package com.example.game.service;

import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.model.SinglePlayerGame;
import com.example.game.repository.SinglePlayerGameRepository;
import com.example.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SinglePlayerGameService {

    private final UserRepository userRepository;
    private final Choices[] choices = Choices.values();
    private final SinglePlayerGameRepository singlePlayerGameRepository;

    public SinglePlayerGame playSinglePlayerGame(Choices playerChoice, String username) {
        Random random = new Random();
        Choices computerChoice = choices[random.nextInt(choices.length)];

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        int currentScore = user.getSinglePlayerScore();

        int newScore = determineWinner(playerChoice, computerChoice, currentScore);

        user.setSinglePlayerScore(newScore);
        userRepository.save(user);

        SinglePlayerGame singlePlayerGame = SinglePlayerGame.builder()
                .playerChoice(playerChoice)
                .computerChoice(computerChoice)
                .result(newScore > currentScore ? "You win!" : (newScore < currentScore ? "You lose!" : "It's a tie!"))
                .time(LocalDateTime.now())
                .user(user)
                .build();

        singlePlayerGameRepository.save(singlePlayerGame);

        return singlePlayerGame;
    }

    private int determineWinner(Choices player, Choices computer, int currentScore) {
        if (player == computer) {
            return currentScore;
        } else if ((player == Choices.ROCK && computer == Choices.SCISSORS) ||
                (player == Choices.SCISSORS && computer == Choices.PAPER) ||
                (player == Choices.PAPER && computer == Choices.ROCK)) {
            return currentScore + 1;
        } else {
            return currentScore;
        }
    }

    public List<SinglePlayerGame> getUserGameHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return singlePlayerGameRepository.findByUser(user);
    }
}