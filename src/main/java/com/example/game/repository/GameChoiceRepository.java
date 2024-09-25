package com.example.game.repository;

import com.example.game.model.GameChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameChoiceRepository extends JpaRepository<GameChoice, Long> {
}