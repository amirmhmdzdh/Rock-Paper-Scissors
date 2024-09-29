package com.example.game.service;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.User;
import com.example.game.model.enume.Choices;
import com.example.game.repository.MultiplayerGameRepository;
import com.example.game.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MultiplayerGameService {
    private final MultiplayerGameRepository multiplayerGameRepository;
    private final UserRepository userRepository;

    public MultiplayerGame createGame(String player1Username, String player2Username) {
        User player1 = userRepository.findByUsername(player1Username);
        User player2 = userRepository.findByUsername(player2Username);

        MultiplayerGame game = new MultiplayerGame();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setRoundsPlayed(0);
        game.setStartTime(LocalDateTime.now());

        return multiplayerGameRepository.save(game);
    }

    public String playRound(Long gameId, String playerUsername, Choices choice) {
        MultiplayerGame game = multiplayerGameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getRoundsPlayed() >= 5) {
            return "The game is already finished!";
        }

        // بررسی نوبت و ذخیره انتخاب
        if (playerUsername.equals(game.getPlayer1().getUsername())) {
            game.setPlayer1Choice(choice);
            game.setPlayer1Ready(true); // بازیکن اول آماده است

            if (game.isPlayer2Ready()) {
                // اگر بازیکن دوم هم آماده است، نتیجه را محاسبه کنید
                return calculateRoundResult(game);
            }
            multiplayerGameRepository.save(game);
            return "Player 1 has chosen. Waiting for Player 2...";

        } else if (playerUsername.equals(game.getPlayer2().getUsername())) {
            game.setPlayer2Choice(choice);
            game.setPlayer2Ready(true); // بازیکن دوم آماده است

            if (game.isPlayer1Ready()) {
                // اگر بازیکن اول هم آماده است، نتیجه را محاسبه کنید
                return calculateRoundResult(game);
            }
            multiplayerGameRepository.save(game);
            return "Player 2 has chosen. Waiting for Player 1...";

        } else {
            return "It's not your turn!";
        }
    }

    // متد برای محاسبه نتیجه دور
    private String calculateRoundResult(MultiplayerGame game) {
        int result = determineRoundWinner(game.getPlayer1Choice(), game.getPlayer2Choice());

        if (result == 1) {
            game.setPlayer1Score(game.getPlayer1Score() + 1);
        } else if (result == -1) {
            game.setPlayer2Score(game.getPlayer2Score() + 1);
        }

        game.setRoundsPlayed(game.getRoundsPlayed() + 1);

        // بررسی پایان بازی
        if (game.getRoundsPlayed() == 5) {
            game.setEndTime(LocalDateTime.now());
            multiplayerGameRepository.save(game);
            return determineWinner(game);
        }

        // ریست کردن وضعیت برای دور بعدی
        game.setPlayer1Ready(false);
        game.setPlayer2Ready(false);

        multiplayerGameRepository.save(game);

        return "Round played successfully! Current score: Player 1 - " + game.getPlayer1Score() + ", Player 2 - " + game.getPlayer2Score();
    }

    private int determineRoundWinner(Choices playerChoice, Choices opponentChoice) {
        if (playerChoice == opponentChoice) {
            return 0;
        } else if ((playerChoice == Choices.ROCK && opponentChoice == Choices.SCISSORS) ||
                (playerChoice == Choices.SCISSORS && opponentChoice == Choices.PAPER) ||
                (playerChoice == Choices.PAPER && opponentChoice == Choices.ROCK)) {
            return 1;
        } else {
            return 0;
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
}