package com.example.game.service;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.repository.MultiplayerGameRepository;
import com.example.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MultiplayerGameService {
    private final UserRepository userRepository;
    private final MultiplayerGameRepository multiplayerGameRepository;

    public MultiplayerGame createGame(String player1Username, String player2Username) {
        User player1 = userRepository.findByUsername(player1Username)
                .orElseThrow(() -> new RuntimeException("Player 1 not found"));
        User player2 = userRepository.findByUsername(player2Username)
                .orElseThrow(() -> new RuntimeException("Player 2 not found"));

        MultiplayerGame game = MultiplayerGame.builder()
                .player1(player1).player2(player2)
                .roundsPlayed(0).startTime(LocalDateTime.now())
                .build();
        return multiplayerGameRepository.save(game);
    }

    public String playRound(Long gameId, String playerUsername, Choices choice) {
        MultiplayerGame game = multiplayerGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        if (game.getRoundsPlayed() >= 5) {
            return "The game is already finished!";
        }

        if (playerUsername.equals(game.getPlayer1().getUsername())) {
            game.setPlayer1Choice(choice);
            game.setPlayer1Ready(true);

            if (game.isPlayer2Ready()) {

                return calculateRoundResult(game);
            }
            multiplayerGameRepository.save(game);
            return "Player 1 has chosen. Waiting for Player 2...";

        } else if (playerUsername.equals(game.getPlayer2().getUsername())) {
            game.setPlayer2Choice(choice);
            game.setPlayer2Ready(true);

            if (game.isPlayer1Ready()) {

                return calculateRoundResult(game);
            }
            multiplayerGameRepository.save(game);
            return "Player 2 has chosen. Waiting for Player 1...";

        } else {
            return "It's not your turn!";
        }
    }

    private String calculateRoundResult(MultiplayerGame game) {
        int result = determineRoundWinner(game.getPlayer1Choice(), game.getPlayer2Choice());

        if (result == 1) {
            game.setPlayer1Score(game.getPlayer1Score() + 1);
        } else if (result == -1) {
            game.setPlayer2Score(game.getPlayer2Score() + 1);
        }

        game.setRoundsPlayed(game.getRoundsPlayed() + 1);

        if (game.getRoundsPlayed() == 5) {
            game.setEndTime(LocalDateTime.now());
            multiplayerGameRepository.save(game);
            return determineWinner(game);
        }
        game.setPlayer1Ready(false);
        game.setPlayer2Ready(false);

        multiplayerGameRepository.save(game);

        return "Round played successfully! Current score: Player 1 - " + game.getPlayer1Score() + ", Player 2 - " +
                game.getPlayer2Score();
    }

    private int determineRoundWinner(Choices playerChoice, Choices opponentChoice) {
        if (playerChoice == opponentChoice) {
            return 0;
        } else if ((playerChoice == Choices.ROCK && opponentChoice == Choices.SCISSORS) ||
                (playerChoice == Choices.SCISSORS && opponentChoice == Choices.PAPER) ||
                (playerChoice == Choices.PAPER && opponentChoice == Choices.ROCK)) {
            return 1;
        } else {
            return -1;
        }
    }

    private String determineWinner(MultiplayerGame game) {
        if (game.getPlayer1Score() > game.getPlayer2Score()) {
            return "Player 1 wins the game!";
        } else if (game.getPlayer2Score() > game.getPlayer1Score()) {
            return "Player 2 wins the game!";
        } else {
            return "The game is a tie!";
        }
    }

    public List<MultiplayerGame> getUserGameHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MultiplayerGame> gamesAsPlayer1 = multiplayerGameRepository.findByPlayer1(user);
        List<MultiplayerGame> gamesAsPlayer2 = multiplayerGameRepository.findByPlayer2(user);

        gamesAsPlayer1.addAll(gamesAsPlayer2);
        return gamesAsPlayer1;
    }
}