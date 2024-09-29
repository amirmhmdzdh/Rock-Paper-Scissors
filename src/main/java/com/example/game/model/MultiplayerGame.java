package com.example.game.model;

import com.example.game.model.enume.Choices;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiplayerGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player1_id")
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private User player2;

    @Enumerated(value = EnumType.STRING)
    private Choices player1Choice;

    @Enumerated(value = EnumType.STRING)
    private Choices player2Choice;


    private int player1Score;

    private int player2Score;

    private int roundsPlayed;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean isPlayer1Ready;

    private boolean isPlayer2Ready;
}