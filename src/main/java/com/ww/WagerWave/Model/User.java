package com.ww.WagerWave.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
/*
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "account_creation", nullable = false, updatable = false)
    private LocalDateTime accountCreation;

    @Column(name = "verified", nullable = false, columnDefinition = "BIT")
    private Boolean verified;

    public User() {
        this.accountCreation = LocalDateTime.now();
    }

    public boolean isAdult() {
        LocalDate today = LocalDate.now();
        return !birthDate.plusYears(18).isAfter(today);
    }
}
*/

