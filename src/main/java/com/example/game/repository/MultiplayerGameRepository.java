package com.example.game.repository;

import com.example.game.model.MultiplayerGame;
import com.example.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MultiplayerGameRepository extends JpaRepository<MultiplayerGame, Long> {

    List<MultiplayerGame> findByPlayer1(User player1);

    List<MultiplayerGame> findByPlayer2(User player2);
}
