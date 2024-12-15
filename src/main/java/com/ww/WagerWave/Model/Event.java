package com.ww.WagerWave.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer id;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "team_1", nullable = false)
    private String team1;

    @Column(name = "team_2", nullable = false)
    private String team2;

    @Column(name = "odds_team_1", nullable = false)
    private BigDecimal oddsTeam1;

    @Column(name = "odds_team_2", nullable = false)
    private BigDecimal oddsTeam2;

    @Column(name = "odds_draw")
    private BigDecimal oddsDraw;

    @Column(name = "event_start_time", nullable = false)
    private LocalDateTime eventStartTime;

    @Column(name = "event_end_time", nullable = false)
    private LocalDateTime eventEndTime;

    @Column(name = "event_result", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventResult eventResult;

}

enum EventResult {
    PENDING, TEAM_1, TEAM_2, DRAW
}
