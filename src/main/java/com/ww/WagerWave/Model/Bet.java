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
@AllArgsConstructor
@Table(name = "bets")
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bet_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "bet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BetType betType;

    @Column(name = "bet_odds", nullable = false)
    private BigDecimal odds;

    @Column(name = "bet_stake")
    private BigDecimal stake;

    @Column(name = "bet_potential_win")
    private BigDecimal potentialWin;

    @Column(name = "bet_creation", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "bet_end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "bet_result", nullable = false)
    @Enumerated(EnumType.STRING)
    private BetResult result;

    public Bet() {
        this.creationTime = LocalDateTime.now();
    }
}

enum BetType {
    TEAM_1, TEAM_2, DRAW
}

enum BetResult {
    PENDING, WON, LOST
}
