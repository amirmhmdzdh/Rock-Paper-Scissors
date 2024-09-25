package com.example.game.model;

import com.example.game.model.enume.Choices;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
public class GameChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private Choices playerChoice;

    @Enumerated(value = EnumType.STRING)
    private Choices computerChoice;

    private String result;

    private LocalDateTime time;
}
