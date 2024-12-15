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
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private MyUser user;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "remaining_limit")
    private BigDecimal remainingLimit;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
