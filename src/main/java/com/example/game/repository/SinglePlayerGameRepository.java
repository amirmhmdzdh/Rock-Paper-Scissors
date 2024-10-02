package com.example.game.repository;

import com.example.game.model.SinglePlayerGame;
import com.example.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SinglePlayerGameRepository extends JpaRepository<SinglePlayerGame, Long> {

    List<SinglePlayerGame> findByUser(User user);

}