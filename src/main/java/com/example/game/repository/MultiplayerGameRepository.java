package com.example.game.repository;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiplayerGameRepository extends JpaRepository<MultiplayerGame, Long> {
    List<MultiplayerGame> findByPlayer1OrPlayer2(User player1, User player2);
}