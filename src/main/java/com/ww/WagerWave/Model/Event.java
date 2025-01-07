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

    @Column(name ="subcategory", nullable = false)
    private String subcategory;

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

    //nowe kolumny potrzebne do pobieranie danych z api
    @Column(name = "api_game_id", nullable = false)
    private String apiGameId; // Kolumna 'api_game_id'

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.SCHEDULED;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

}

