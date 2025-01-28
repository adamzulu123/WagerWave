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
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private MyUser user;

    @Column(name = "coupon_odds", nullable = false)
    private BigDecimal odds;

    @Column(name = "coupon_stake", nullable = false)
    private BigDecimal stake;

    @Column(name = "coupon_potential_win", nullable = false)
    private BigDecimal potentialWin;

    @Column(name = "coupon_creation", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "coupon_end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "coupon_result", nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponResult result;

    public Coupon() {
        this.creationTime = LocalDateTime.now();
    }
}

